package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.bonuses.MILITARY_BONUS_STATUS;
import com.example.minitaxiandroid.entities.bonuses.MilitaryBonuses;
import com.example.minitaxiandroid.entities.messages.Message;
import com.example.minitaxiandroid.entities.ranks.EliteRankUserInfo;
import com.example.minitaxiandroid.entities.ranks.Rank;
import com.example.minitaxiandroid.entities.ranks.UserEliteRankAchievementInfo;
import com.example.minitaxiandroid.entities.ranks.UserRankAchievementInfo;
import com.example.minitaxiandroid.entities.userinfo.UserStats;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import com.example.minitaxiandroid.services.UserLoginInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RanksInfoActivity extends AppCompatActivity {

    private TextView cossackTextView, cossackConditionInfoTextView, cossackBonusesInfoTextView, kyrinnuyKozakTextView,
            kyrinnuyKozakConditionsInfoTextView, kyrinnuyKozakBonusesInfoTextView, sotnukTextView,
            sotnukConditionsInfoTextView, sotnukBonusesInfoTextView, polkovnukKozakTextView,
            polkovnukKozakConditionsInfoTextView, polkovnukKozakBonusesInfoTextView, koshoviyOtamanTextView,
            koshoviyOtamanConditionsInfoTextView, koshoviyOtamanBonusesInfoTextView, hetmanTextView,
            hetmanConditionsInfoTextView, hetmanBonusesInfoTextView;
    private TextView orderCountTextView, commentsCountTextView, saleCountTextView, saleDeadlineTextView,
            standardFreeOrderCountTextView, standardDeadlineTextView, comfortFreeOrderCountTextView,
            comfortDeadlineTextView, eliteFreeOrderCountTextView, eliteDeadlineTextView;
    private TextView militaryBonusesInfoText;
    private List<TextView> rankList;
    private List<TextView> eliteRankList;;
    private boolean isMilitaryBonusesSend = false;
    private ActivityResultLauncher<String> loadImageActivityResultLauncher;
    private MilitaryBonuses militaryBonuses;
    private Button militaryBonusesButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranks_info);
        backButton = findViewById(R.id.ranksBackButton);
        militaryBonusesButton = findViewById(R.id.militaryBonusesButton);
        militaryBonusesInfoText = findViewById(R.id.militaryBonusesInfoText);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        loadImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            InputStream is = getContentResolver().openInputStream(result);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
                            byte[] byteArray = stream.toByteArray();
                            sendMilitaryBonusesDocumentPhoto(byteArray);
                            StringBuilder newString = new StringBuilder(getResources().
                                    getString(R.string.military_bonuses_waiting_notice));
                            newString.append(" ").append(militaryBonuses.getSaleValue()).append("%");
                            militaryBonusesButton.setText(getResources().getString(R.string.military_bonuses_delete_button));
                            militaryBonusesInfoText.setText(newString);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        textViewRanksInfoInitialize();
        textViewStatsInfoInitialize();
        getMilitaryBonusesInfoRequest();
        militaryBonusesButton.setOnClickListener(view -> {
            System.out.println(isMilitaryBonusesSend);
            if(isMilitaryBonusesSend){
                deleteMilitaryBonusesRequest();
            }
            else {
                loadDocument();
            }
        });
        setUserStats();
        setRanksStats();
        getRanksInfoRequest();
        getEliteRanksInfoRequest();
    }

    private void goHome(){
        Intent intent = new Intent(RanksInfoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void sendMilitaryBonusesDocumentPhoto(byte[] photo){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        MilitaryBonuses militaryBonuses = new MilitaryBonuses(-1,
                Integer.parseInt(UserLoginInfoService.getProperty("userId")),
                photo,
                MILITARY_BONUS_STATUS.WAITING,
                -1,
                null
                );
        rankInfoApi.addMilitaryBonuses(militaryBonuses)
                .enqueue(new Callback<Message>() {

                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.body().equals("success")) {
                            militaryBonusesButton.setText(getResources().getString(R.string.military_bonuses_delete_button));
                            isMilitaryBonusesSend = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(RanksInfoActivity.this,
                                getResources().getString(R.string.error_to_get_user_stats),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUserStats(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserStats(Integer.parseInt(UserLoginInfoService.getProperty("userId")))
                .enqueue(new Callback<UserStats>() {

                    @Override
                    public void onResponse(Call<UserStats> call, Response<UserStats> response) {
                        orderCountTextView.setText(String.valueOf(response.body().getCountOrders()));
                        commentsCountTextView.setText(String.valueOf(response.body().getCountComments()));
                    }

                    @Override
                    public void onFailure(Call<UserStats> call, Throwable t) {
                        Toast.makeText(RanksInfoActivity.this,
                                getResources().getString(R.string.error_to_get_user_stats),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setRanksStats(){
        UserLoginInfoService.addProperty("rankId", "6");
        if(Integer.parseInt(UserLoginInfoService.getProperty("rankId")) == 1){
          setBasicStats();
        }
        else {
            getBaseRankUserStatsRequest();
            if(Integer.parseInt(UserLoginInfoService.getProperty("rankId"))>4){
                getEliteRankUserStatsRequest();
            }
        }
    }

    private void setBasicStats(){
        saleCountTextView.setText("0");
        standardFreeOrderCountTextView.setText("0");
        comfortFreeOrderCountTextView.setText("0");
        eliteFreeOrderCountTextView.setText("0");
        String deadlineText = getResources().getString(R.string.deadline) + " None";
        standardDeadlineTextView.setText(deadlineText);
        comfortDeadlineTextView.setText(deadlineText);
        eliteDeadlineTextView.setText(deadlineText);
    }

    public void getBaseRankUserStatsRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserRankAchievementsInfo(Integer.parseInt(UserLoginInfoService.getProperty("userId")),
                        Integer.parseInt(UserLoginInfoService.getProperty("rankId")))
                .enqueue(new Callback<UserRankAchievementInfo>() {

                    @Override
                    public void onResponse(Call<UserRankAchievementInfo> call, Response<UserRankAchievementInfo> response) {
                        setBaseRankStats(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserRankAchievementInfo> call, Throwable t) {
                        Toast.makeText(RanksInfoActivity.this,
                                getResources().getString(R.string.error_to_get_base_rank_user_info),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setBaseRankStats(UserRankAchievementInfo response){
        int countOfSaleOrders = response.getNumberOfUsesSale();
        saleCountTextView.setText(String.valueOf(countOfSaleOrders));
        String message;
        if(countOfSaleOrders == 0){
            LocalDate date = Instant.ofEpochMilli(response.getDeadlineDateSale().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            message = getResources().getString(R.string.wait_until) + " " +
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        else {
            message = getResources().getString(R.string.deadline) + " " +
                    response.getDeadlineDateSale().getTime();
        }
        saleDeadlineTextView.setText(message);
        String deadlineText = getResources().getString(R.string.deadline) + " None";
        standardFreeOrderCountTextView.setText("0");
        comfortFreeOrderCountTextView.setText("0");
        eliteFreeOrderCountTextView.setText("0");
        standardDeadlineTextView.setText(deadlineText);
        comfortDeadlineTextView.setText(deadlineText);
        eliteDeadlineTextView.setText(deadlineText);
    }

    public void getEliteRankUserStatsRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserEliteRankAchievementsInfo(Integer.parseInt(UserLoginInfoService.getProperty("userId")),
                        Integer.parseInt(UserLoginInfoService.getProperty("rankId")))
                .enqueue(new Callback<List<UserEliteRankAchievementInfo>>() {

                    @Override
                    public void onResponse(Call<List<UserEliteRankAchievementInfo>> call,
                                           Response<List<UserEliteRankAchievementInfo>> response) {
                        setEliteRanksStats(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<UserEliteRankAchievementInfo>> call, Throwable t) {
                        Toast.makeText(RanksInfoActivity.this,
                                getResources().getString(R.string.error_to_get_base_rank_user_info),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEliteRanksStats(List<UserEliteRankAchievementInfo> eliteRanksStats){
        if(eliteRanksStats.size() == 1){
            standardFreeOrderCountTextView.setText(String.valueOf(eliteRanksStats.get(0).getNumberOfUsesFreeOrder()));
            String deadlineStandardText = setDeadlineText(eliteRanksStats.get(0).getNumberOfUsesFreeOrder(),
                    eliteRanksStats.get(0).getDeadlineDateFreeOrder());
            standardDeadlineTextView.setText(deadlineStandardText);
            String deadlineText = getResources().getString(R.string.deadline) + " None";
            comfortDeadlineTextView.setText(deadlineText);
            eliteDeadlineTextView.setText(deadlineText);
        }
        else if(eliteRanksStats.size() == 2){
            standardFreeOrderCountTextView.setText(String.valueOf(eliteRanksStats.get(0).getNumberOfUsesFreeOrder()));
            comfortFreeOrderCountTextView.setText(String.valueOf(eliteRanksStats.get(1).getNumberOfUsesFreeOrder()));
            String deadlineStandardText = setDeadlineText(eliteRanksStats.get(0).getNumberOfUsesFreeOrder(),
                    eliteRanksStats.get(0).getDeadlineDateFreeOrder());
            String deadlineComfortText = setDeadlineText(eliteRanksStats.get(1).getNumberOfUsesFreeOrder(),
                    eliteRanksStats.get(1).getDeadlineDateFreeOrder());
            standardDeadlineTextView.setText(deadlineStandardText);
            comfortDeadlineTextView.setText(deadlineComfortText);
            String deadlineText = getResources().getString(R.string.deadline) + " None";
            eliteDeadlineTextView.setText(deadlineText);
        }
        else if(eliteRanksStats.size() == 3){
            standardFreeOrderCountTextView.setText(String.valueOf(eliteRanksStats.get(0).getNumberOfUsesFreeOrder()));
            comfortFreeOrderCountTextView.setText(String.valueOf(eliteRanksStats.get(1).getNumberOfUsesFreeOrder()));
            eliteFreeOrderCountTextView.setText(String.valueOf(eliteRanksStats.get(2).getNumberOfUsesFreeOrder()));
            String deadlineStandardText = setDeadlineText(eliteRanksStats.get(0).getNumberOfUsesFreeOrder(),
                    eliteRanksStats.get(1).getDeadlineDateFreeOrder());
            String deadlineComfortText = setDeadlineText(eliteRanksStats.get(0).getNumberOfUsesFreeOrder(),
                    eliteRanksStats.get(2).getDeadlineDateFreeOrder());
            String deadlineEliteText = setDeadlineText(eliteRanksStats.get(0).getNumberOfUsesFreeOrder(),
                    eliteRanksStats.get(3).getDeadlineDateFreeOrder());
            standardDeadlineTextView.setText(deadlineStandardText);
            comfortDeadlineTextView.setText(deadlineComfortText);
            eliteDeadlineTextView.setText(deadlineEliteText);
        }
    }

    private String setDeadlineText(int count, Timestamp date){
        String deadlineText;
        LocalDate convertedDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        if(count == 0){
            deadlineText = getResources().getString(R.string.wait_until) + " "
                    + convertedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        else{
            deadlineText = getResources().getString(R.string.deadline) + " "
                    + convertedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return deadlineText;
    }

    public void getRanksInfoRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getRankInfo().enqueue(new Callback<List<Rank>>() {
            @Override
            public void onResponse(Call<List<Rank>> call, Response<List<Rank>> response) {
                getRanksInfo(response.body());
            }

            @Override
            public void onFailure(Call<List<Rank>> call, Throwable t) {
                Toast.makeText(RanksInfoActivity.this, "Failed to load ranks info",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRanksInfo(List<Rank> rankList){
        setRank(rankList);
    }

    private void setRank(List<Rank> response){
        response.remove(0);
        for(int i = 0; i < rankList.size(); i++){
            TextView title = rankList.get(i);
            int module = i / 3;
            title.setText(response.get(module).getName());
            String conditions = "Need min orders: " + response.get(module).getMinOrders() +
                    ", also min count of comments " + response.get(module).getMinComments();
            TextView conditionsInfoText = rankList.get(i+1);
            conditionsInfoText.setText(conditions);
            String bonuses = "This rank gives you sale " + response.get(module).getSaleValue() +
                    " percents on order each " + response.get(module).getSalePeriod() + " days";
            TextView bonusesInfoText = rankList.get(i+2);
            bonusesInfoText.setText(bonuses);
            if(i == 6){
                break;
            }
            i = i + 2;
        }
    }

    public void getEliteRanksInfoRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getEliteRanksInfo().enqueue(new Callback<List<EliteRankUserInfo>>() {
            @Override
            public void onResponse(Call<List<EliteRankUserInfo>> call, Response<List<EliteRankUserInfo>> response) {
                getEliteRanksInfo(response.body());
            }

            @Override
            public void onFailure(Call<List<EliteRankUserInfo>> call, Throwable t) {
                Toast.makeText(RanksInfoActivity.this, "Failed to load elite ranks info",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEliteRanksInfo(List<EliteRankUserInfo> userInfos){
        setEliteRanks(userInfos);
    }

    private void setEliteRanks(List<EliteRankUserInfo> response) {
        int count = 0;
        for(int i = 0; i < eliteRankList.size(); i++){
            TextView title = eliteRankList.get(i);
            title.setText(response.get(count).getName());
            String conditions = "Need min orders: " + response.get(count).getMinOrders() +
                    ", also min count of comments" + response.get(count).getMinComments();
            TextView conditionsInfoText = eliteRankList.get(i+1);
            conditionsInfoText.setText(conditions);
            StringBuilder bonuses = new StringBuilder("This rank gives you sale " + response.get(count).getSaleValue() +
                    " percents on order each " + response.get(count).getSalePeriod() + " days, also " +
                    response.get(count).getFreeOrders() + " free orders on " +
                    response.get(count).getCarClassName() + " class each " + response.get(count).getPeriod() +
                    " days, ");
            for(int j = count + 1; j < response.size(); j++) {
                if(j > 0) {
                    if (response.get(j).getRanksId() > response.get(j - 1).getRanksId()) {
                        count = j;
                        break;
                    }
                }
                bonuses.append(response.get(j).getFreeOrders()).append(" free orders on ").append(response.get(j).getCarClassName()).
                        append(" class each ").append(response.get(j).getPeriod()).append(" days");
            }
            TextView bonusesInfoText = eliteRankList.get(i + 2);
            bonusesInfoText.setText(bonuses.toString());
            if(i == 6){
                break;
            }
            i = i + 2;
        }
    }

    public void getMilitaryBonusesInfoRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi militaryBonusesApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        militaryBonusesApi.getMilitaryBonuses(Integer.parseInt(UserLoginInfoService.getProperty("userId"))).
                enqueue(new Callback<MilitaryBonuses>() {
                    @Override
                    public void onResponse(Call<MilitaryBonuses> call, Response<MilitaryBonuses> response) {
                        getMilitaryBonusesInfo(response.body());
                    }

                    @Override
                    public void onFailure(Call<MilitaryBonuses> call, Throwable t) {
                        Toast.makeText(RanksInfoActivity.this, getResources().getString(R.string.failed_get_military_bonuses),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getMilitaryBonusesInfo(MilitaryBonuses response){
        this.militaryBonuses = response;
        if(response.getMilitaryBonusesId() == 0 || response.getMILITARYBONUSEStatus().equals(MILITARY_BONUS_STATUS.REJECT)){
            StringBuilder newString = new StringBuilder(getResources().
                    getString(R.string.military_bonuses_notice));
            newString.append(" ").append(response.getSaleValue());
            militaryBonusesInfoText.setText(newString.toString());
        }
        else {
            if(response.getMILITARYBONUSEStatus().equals(MILITARY_BONUS_STATUS.WAITING)){
                isMilitaryBonusesSend = true;
                StringBuilder newString = new StringBuilder(getResources().
                        getString(R.string.military_bonuses_waiting_notice));
                newString.append(" ").append(response.getSaleValue()).append("%");
                militaryBonusesInfoText.setText(newString.toString());
                militaryBonusesButton.setText(getResources().getString(R.string.military_bonuses_delete_button));
            }
            else if(response.getMILITARYBONUSEStatus().equals(MILITARY_BONUS_STATUS.COMPLETE)){
                isMilitaryBonusesSend = true;
                StringBuilder newString = new StringBuilder(getResources().
                        getString(R.string.military_bonuses_complete_notice));
                newString.append(" ").append(response.getSaleValue()).append("%");
                militaryBonusesInfoText.setText(newString.toString());
                militaryBonusesButton.setText(getResources().getString(R.string.military_bonuses_delete_button));
            }
        }
    }

    public void deleteMilitaryBonusesRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi militaryBonusesApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        militaryBonusesApi.deleteMilitaryBonuses(militaryBonuses.getMilitaryBonusesId()).
                enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        isMilitaryBonusesSend = false;
                        StringBuilder newString = new StringBuilder(getResources().
                                getString(R.string.military_bonuses_notice));
                        newString.append(" ").append(militaryBonuses.getSaleValue());
                        militaryBonusesInfoText.setText(newString);
                        militaryBonusesButton.setText(getResources().getString(R.string.load_document));
                        Toast.makeText(RanksInfoActivity.this, getResources().getString(R.string.military_bonuses_delete_notice),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(RanksInfoActivity.this, "Failed to delete your document",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadDocument(){
        loadImageActivityResultLauncher.launch("image/*");
    }

    private void textViewStatsInfoInitialize(){
        orderCountTextView=findViewById(R.id.orderCountTextView);
        commentsCountTextView=findViewById(R.id.commentsCountTextView);
        saleCountTextView=findViewById(R.id.saleCountTextView);
        saleDeadlineTextView=findViewById(R.id.saleDeadlineTextView);
        standardFreeOrderCountTextView=findViewById(R.id.standardFreeOrderCountTextView);
        standardDeadlineTextView=findViewById(R.id.standardDeadlineTextView);
        comfortFreeOrderCountTextView=findViewById(R.id.comfortFreeOrderCountTextView);
        comfortDeadlineTextView=findViewById(R.id.comfortDeadlineTextView);
        eliteFreeOrderCountTextView=findViewById(R.id.eliteFreeOrderCountTextView);
        eliteDeadlineTextView=findViewById(R.id.eliteDeadlineTextView);
    }
    private void textViewRanksInfoInitialize(){
        rankList = new ArrayList<>();
        eliteRankList = new ArrayList<>();
        cossackTextView = findViewById(R.id.cossackTextView);
        rankList.add(cossackTextView);
        cossackConditionInfoTextView = findViewById(R.id.cossackConditionInfoTextView);
        rankList.add(cossackConditionInfoTextView);
        cossackBonusesInfoTextView = findViewById(R.id.cossackBonusesInfoTextView);
        rankList.add(cossackBonusesInfoTextView);
        kyrinnuyKozakTextView = findViewById(R.id.kyrinnuyKozakTextView);
        rankList.add(kyrinnuyKozakTextView);
        kyrinnuyKozakConditionsInfoTextView = findViewById(R.id.kyrinnuyKozakConditionsInfoTextView);
        rankList.add(kyrinnuyKozakConditionsInfoTextView);
        kyrinnuyKozakBonusesInfoTextView = findViewById(R.id.kyrinnuyKozakBonusesInfoTextView);
        rankList.add(kyrinnuyKozakBonusesInfoTextView);
        sotnukTextView = findViewById(R.id.sotnukTextView);
        rankList.add(sotnukTextView);
        sotnukConditionsInfoTextView = findViewById(R.id.sotnukConditionsInfoTextView);
        rankList.add(sotnukConditionsInfoTextView);
        sotnukBonusesInfoTextView = findViewById(R.id.sotnukBonusesInfoTextView);
        rankList.add(sotnukBonusesInfoTextView);
        polkovnukKozakTextView = findViewById(R.id.polkovnukKozakTextView);
        eliteRankList.add(polkovnukKozakTextView);
        polkovnukKozakConditionsInfoTextView = findViewById(R.id.polkovnukKozakConditionsInfoTextView);
        eliteRankList.add(polkovnukKozakConditionsInfoTextView);
        polkovnukKozakBonusesInfoTextView = findViewById(R.id.polkovnukKozakBonusesInfoTextView);
        eliteRankList.add(polkovnukKozakBonusesInfoTextView);
        koshoviyOtamanTextView = findViewById(R.id.koshoviyOtamanTextView);
        eliteRankList.add(koshoviyOtamanTextView);
        koshoviyOtamanConditionsInfoTextView = findViewById(R.id.koshoviyOtamanConditionsInfoTextView);
        eliteRankList.add(koshoviyOtamanConditionsInfoTextView);
        koshoviyOtamanBonusesInfoTextView = findViewById(R.id.koshoviyOtamanBonusesInfoTextView);
        eliteRankList.add(koshoviyOtamanBonusesInfoTextView);
        hetmanTextView = findViewById(R.id.hetmanTextView);
        eliteRankList.add(hetmanTextView);
        hetmanConditionsInfoTextView = findViewById(R.id.hetmanConditionsInfoTextView);
        eliteRankList.add(hetmanConditionsInfoTextView);
        hetmanBonusesInfoTextView = findViewById(R.id.hetmanBonusesInfoTextView);
        eliteRankList.add(hetmanBonusesInfoTextView);
    }
}