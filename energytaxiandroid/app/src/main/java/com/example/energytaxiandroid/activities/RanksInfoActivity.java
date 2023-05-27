package com.example.energytaxiandroid.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.energytaxiandroid.R;
import com.example.energytaxiandroid.api.MiniTaxiApi;
import com.example.energytaxiandroid.api.RetrofitService;
import com.example.energytaxiandroid.entities.auth.RegisterResponse;
import com.example.energytaxiandroid.entities.bonuses.MILITARY_BONUS_STATUS;
import com.example.energytaxiandroid.entities.bonuses.MilitaryBonuses;
import com.example.energytaxiandroid.entities.messages.MyMessage;
import com.example.energytaxiandroid.entities.ranks.EliteRankUserInfo;
import com.example.energytaxiandroid.entities.ranks.Rank;
import com.example.energytaxiandroid.entities.ranks.UserEliteRankAchievementInfo;
import com.example.energytaxiandroid.entities.ranks.UserRankAchievementInfo;
import com.example.energytaxiandroid.entities.userinfo.UserStats;
import com.example.energytaxiandroid.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private Rank userRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranks_info);
        backButton = findViewById(R.id.ranksBackButton);
        militaryBonusesButton = findViewById(R.id.militaryBonusesButton);
        militaryBonusesInfoText = findViewById(R.id.militaryBonusesInfoText);
        backButton.setOnClickListener(view -> goHome());
        UserInfoService.init(RanksInfoActivity.this);
        loadImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    try {
                        if(result != null) {
                            InputStream is = getContentResolver().openInputStream(result);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                            byte[] byteArray = stream.toByteArray();
                            sendMilitaryBonusesDocumentPhoto(byteArray);
                            StringBuilder newString = new StringBuilder(getResources().
                                    getString(R.string.military_bonuses_waiting_notice));
                            newString.append(" ").append(militaryBonuses.getSaleValue()).append("%");
                            militaryBonusesButton.setText(getResources().getString(R.string.military_bonuses_delete_button));
                            militaryBonusesInfoText.setText(newString);
                            isMilitaryBonusesSend = true;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );
        textViewRanksInfoInitialize();
        textViewStatsInfoInitialize();
        getMilitaryBonusesInfoRequest();
        militaryBonusesButton.setOnClickListener(view -> {
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
                Integer.parseInt(UserInfoService.getProperty("userId")),
                photo,
                MILITARY_BONUS_STATUS.WAITING,
                -1,
                null
                );
        rankInfoApi.addMilitaryBonuses("Bearer " + UserInfoService.getProperty("access_token"),
                        militaryBonuses)
                .enqueue(new Callback<MyMessage>() {

                    @Override
                    public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    if (response.body().getContent().equals("success")) {
                                        militaryBonusesButton.setText(getResources().getString(R.string.military_bonuses_delete_button));
                                        isMilitaryBonusesSend = true;
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyMessage> call, Throwable t) {
                        RanksInfoActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(RanksInfoActivity.this,
                                        getResources().getString(R.string.error_to_get_user_stats),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }

    private void setUserStats(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserStats("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.parseInt(UserInfoService.getProperty("userId")))
                .enqueue(new Callback<UserStats>() {

                    @Override
                    public void onResponse(Call<UserStats> call, Response<UserStats> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    orderCountTextView.setText(String.valueOf(response.body().getCountOrders()));
                                    commentsCountTextView.setText(String.valueOf(response.body().getCountComments()));
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserStats> call, Throwable t) {
                        RanksInfoActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(RanksInfoActivity.this,
                                        getResources().getString(R.string.error_to_get_user_stats),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }

    private void setRanksStats(){
        if(Integer.parseInt(UserInfoService.getProperty("rankId")) == 1){
          setBasicStats();
        }
    }

    private void setBasicStats(){
        saleCountTextView.setText("0");
        standardFreeOrderCountTextView.setText("0");
        comfortFreeOrderCountTextView.setText("0");
        eliteFreeOrderCountTextView.setText("0");
        String deadlineText = getResources().getString(R.string.deadline) + " None ";
        standardDeadlineTextView.setText(deadlineText);
        comfortDeadlineTextView.setText(deadlineText);
        eliteDeadlineTextView.setText(deadlineText);
    }

    public void getBaseRankUserStatsRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserRankAchievementsInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.parseInt(UserInfoService.getProperty("userId")),
                        Integer.parseInt(UserInfoService.getProperty("rankId")))
                .enqueue(new Callback<UserRankAchievementInfo>() {

                    @Override
                    public void onResponse(Call<UserRankAchievementInfo> call, Response<UserRankAchievementInfo> response) {
                        if(response.body()!=null) {
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                } else {
                                    setBaseRankStats(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRankAchievementInfo> call, Throwable t) {
                        RanksInfoActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(RanksInfoActivity.this,
                                        getResources().getString(R.string.error_to_get_base_rank_user_info),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void setBaseRankStats(UserRankAchievementInfo response){
        int countOfSaleOrders = response.getNumberOfUsesSale();
        String sale = countOfSaleOrders + " (" + userRank.getSaleValue() + "%)";
        saleCountTextView.setText(sale);
        String message;
        LocalDate date = Instant.ofEpochMilli(response.getDeadlineDateSale().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        if(countOfSaleOrders == 0){
            message = getResources().getString(R.string.wait_until) + " " +
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        else {
            message = getResources().getString(R.string.deadline) + " " +
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        saleDeadlineTextView.setText(message);
        if(Integer.parseInt(UserInfoService.getProperty("rankId")) < 5) {
            String deadlineText = getResources().getString(R.string.deadline) + " None";
            standardFreeOrderCountTextView.setText("0");
            comfortFreeOrderCountTextView.setText("0");
            eliteFreeOrderCountTextView.setText("0");
            standardDeadlineTextView.setText(deadlineText);
            comfortDeadlineTextView.setText(deadlineText);
            eliteDeadlineTextView.setText(deadlineText);
        }
    }

    public void getEliteRankUserStatsRequest(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getUserEliteRankAchievementsInfo("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.parseInt(UserInfoService.getProperty("userId")),
                        Integer.parseInt(UserInfoService.getProperty("rankId")))
                .enqueue(new Callback<List<UserEliteRankAchievementInfo>>() {

                    @Override
                    public void onResponse(Call<List<UserEliteRankAchievementInfo>> call,
                                           Response<List<UserEliteRankAchievementInfo>> response) {
                        if(response.body()!=null){
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    setEliteRanksStats(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserEliteRankAchievementInfo>> call, Throwable t) {
                        RanksInfoActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(RanksInfoActivity.this,
                                        getResources().getString(R.string.error_to_get_base_rank_user_info),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
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
                    eliteRanksStats.get(0).getDeadlineDateFreeOrder());
            String deadlineComfortText = setDeadlineText(eliteRanksStats.get(0).getNumberOfUsesFreeOrder(),
                    eliteRanksStats.get(1).getDeadlineDateFreeOrder());
            String deadlineEliteText = setDeadlineText(eliteRanksStats.get(0).getNumberOfUsesFreeOrder(),
                    eliteRanksStats.get(2).getDeadlineDateFreeOrder());
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
        rankInfoApi.getRankInfo("Bearer " + UserInfoService.getProperty("access_token")).enqueue(new Callback<List<Rank>>() {
            @Override
            public void onResponse(Call<List<Rank>> call, Response<List<Rank>> response) {
                if(response.body() != null) {
                    try {
                        if (response.errorBody() != null) {
                            if (response.errorBody().string().contains(getResources()
                                    .getString(R.string.token_expired))) {
                                refreshToken();
                            }
                        } else {
                            getRanksInfo(response.body());
                            if (Integer.parseInt(UserInfoService.getProperty("rankId")) != 1) {
                                getBaseRankUserStatsRequest();
                            }
                            if(Integer.parseInt(UserInfoService.getProperty("rankId"))>4){
                                getEliteRankUserStatsRequest();
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Rank>> call, Throwable t) {
                Toast.makeText(RanksInfoActivity.this, "Failed to load ranks info",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRanksInfo(List<Rank> rankList){
        for(Rank rank: rankList){
            if(rank.getRankId() == Integer.parseInt(UserInfoService.getProperty("rankId"))){
                userRank = rank;
            }
        }
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
        rankInfoApi.getEliteRanksInfo("Bearer " + UserInfoService.getProperty("access_token")).
                enqueue(new Callback<List<EliteRankUserInfo>>() {
            @Override
            public void onResponse(Call<List<EliteRankUserInfo>> call, Response<List<EliteRankUserInfo>> response) {
                if(response.body()!=null) {
                    try {
                        if(response.errorBody() != null){
                            if(response.errorBody().string().contains(getResources()
                                    .getString(R.string.token_expired))){
                                refreshToken();
                            }
                        }else {
                            getEliteRanksInfo(response.body());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EliteRankUserInfo>> call, Throwable t) {
                RanksInfoActivity.this.runOnUiThread(() ->
                        Toast.makeText(RanksInfoActivity.this, "Failed to load elite ranks info",
                        Toast.LENGTH_SHORT).show());

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
        militaryBonusesApi.getMilitaryBonuses("Bearer " + UserInfoService.getProperty("access_token"),
                        Integer.parseInt(UserInfoService.getProperty("userId"))).
                enqueue(new Callback<MilitaryBonuses>() {
                    @Override
                    public void onResponse(Call<MilitaryBonuses> call, Response<MilitaryBonuses> response) {
                        if(response.body()!=null) {
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }
                                else{
                                    getMilitaryBonusesInfo(response.body());
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MilitaryBonuses> call, Throwable t) {
                        RanksInfoActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(RanksInfoActivity.this,
                                        getResources().getString(R.string.failed_get_military_bonuses),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }

    public void getMilitaryBonusesInfo(MilitaryBonuses response){
        this.militaryBonuses = response;
        if(response.getMilitaryBonusesId() == 0 || response.getMilitaryBonusStatus().name().
                equals(MILITARY_BONUS_STATUS.REJECT.name())){
            StringBuilder newString = new StringBuilder(getResources().
                    getString(R.string.military_bonuses_notice));
            newString.append(" ").append(response.getSaleValue());
            militaryBonusesInfoText.setText(newString.toString());
        }
        else {
            if(response.getMilitaryBonusStatus().name().equals(MILITARY_BONUS_STATUS.WAITING.name())){
                isMilitaryBonusesSend = true;
                StringBuilder newString = new StringBuilder(getResources().
                        getString(R.string.military_bonuses_waiting_notice));
                newString.append(" ").append(response.getSaleValue()).append("%");
                militaryBonusesInfoText.setText(newString.toString());
                militaryBonusesButton.setText(getResources().getString(R.string.military_bonuses_delete_button));
            }
            else if(response.getMilitaryBonusStatus().name().equals(MILITARY_BONUS_STATUS.COMPLETE.name())){
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
        militaryBonusesApi.deleteMilitaryBonuses("Bearer " + UserInfoService.getProperty("access_token"),
                        militaryBonuses.getMilitaryBonusesId()).
                enqueue(new Callback<MyMessage>() {
                    @Override
                    public void onResponse(Call<MyMessage> call, Response<MyMessage> response) {
                        if(response.body()!=null) {
                            try {
                                if(response.errorBody() != null){
                                    if(response.errorBody().string().contains(getResources()
                                            .getString(R.string.token_expired))){
                                        refreshToken();
                                    }
                                }else {
                                    isMilitaryBonusesSend = false;
                                    StringBuilder newString = new StringBuilder(getResources().
                                            getString(R.string.military_bonuses_notice));
                                    newString.append(" ").append(militaryBonuses.getSaleValue());
                                    militaryBonusesInfoText.setText(newString);
                                    militaryBonusesButton.setText(getResources().getString(R.string.load_document));
                                    Toast.makeText(RanksInfoActivity.this, getResources().getString(R.string.military_bonuses_delete_notice),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyMessage> call, Throwable t) {
                        RanksInfoActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(RanksInfoActivity.this,
                                        "Failed to delete your document",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

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

    private void refreshToken() {
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi api = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        api.refreshToken("Bearer " + UserInfoService.getProperty("refresh_token"))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if(response.body() != null){
                            RegisterResponse registerResponse = response.body();
                            if(registerResponse.getAccessToken().equals(getResources()
                                    .getString(R.string.token_expired))){
                                goLogin();
                            }
                            else if(registerResponse.getAccessToken().equals(getResources()
                                    .getString(R.string.username_not_found))){
                                goLogin();
                            }
                            else {
                                UserInfoService.addProperty("access_token", registerResponse.getAccessToken());
                                UserInfoService.addProperty("refresh_token", registerResponse.getRefreshToken());
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        RanksInfoActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(RanksInfoActivity.this, "Failed to check user authentication",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void goLogin() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
    }
}