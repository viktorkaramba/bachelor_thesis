package com.example.minitaxiandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.entities.ranks.EliteRankUserInfo;
import com.example.minitaxiandroid.entities.ranks.Rank;
import com.example.minitaxiandroid.retrofit.MiniTaxiApi;
import com.example.minitaxiandroid.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class RanksInfoActivity extends AppCompatActivity {

    private TextView cossackTextView, cossackConditionInfoTextView, cossackBonusesInfoTextView, kyrinnuyKozakTextView,
            kyrinnuyKozakConditionsInfoTextView, kyrinnuyKozakBonusesInfoTextView, sotnukTextView,
            sotnukConditionsInfoTextView, sotnukBonusesInfoTextView, polkovnukKozakTextView,
            polkovnukKozakConditionsInfoTextView, polkovnukKozakBonusesInfoTextView, koshoviyOtamanTextView,
            koshoviyOtamanConditionsInfoTextView, koshoviyOtamanBonusesInfoTextView, hetmanTextView,
            hetmanConditionsInfoTextView, hetmanBonusesInfoTextView;

    private List<TextView> rankList;
    private List<TextView> eliteRankList;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranks_info);
        backButton = findViewById(R.id.ranksBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });
        textViewInitialize();
        getRanksInfo();
        getEliteRanksInfo();
    }

    private void goHome(){
        Intent intent = new Intent(RanksInfoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void getRanksInfo(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getRankInfo().enqueue(new Callback<List<Rank>>() {
            @Override
            public void onResponse(Call<List<Rank>> call, Response<List<Rank>> response) {
                setRank(response);
            }

            @Override
            public void onFailure(Call<List<Rank>> call, Throwable t) {
                Toast.makeText(RanksInfoActivity.this, "Failed to load ranks info",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRank(Response<List<Rank>> response) {
        response.body().remove(0);
        for(int i = 0; i < rankList.size(); i++){
            TextView title = rankList.get(i);
            int module = i / 3;
            System.out.println("module: " + module);
            assert response.body() != null;
            title.setText(response.body().get(module).getName());
            String conditions = "Need min orders: " + response.body().get(module).getMinOrders() +
                    ", also min count of comments " + response.body().get(module).getMinComments();
            TextView conditionsInfoText = rankList.get(i+1);
            conditionsInfoText.setText(conditions);
            String bonuses = "This rank gives you sale " + response.body().get(module).getSaleValue() +
                    " percents on order each " + response.body().get(module).getSalePeriod() + " days";
            TextView bonusesInfoText = rankList.get(i+2);
            bonusesInfoText.setText(bonuses);
            if(i == 6){
                break;
            }
            i = i + 2;
        }
    }

    private void getEliteRanksInfo(){
        RetrofitService retrofitService = new RetrofitService();
        MiniTaxiApi rankInfoApi = retrofitService.getRetrofit().create(MiniTaxiApi.class);
        rankInfoApi.getEliteRanksInfo().enqueue(new Callback<List<EliteRankUserInfo>>() {
            @Override
            public void onResponse(Call<List<EliteRankUserInfo>> call, Response<List<EliteRankUserInfo>> response) {
                setEliteRanks(response);
            }

            @Override
            public void onFailure(Call<List<EliteRankUserInfo>> call, Throwable t) {
                Toast.makeText(RanksInfoActivity.this, "Failed to load elite ranks info",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEliteRanks(Response<List<EliteRankUserInfo>> response) {
        int count = 0;
        for(int i = 0; i < eliteRankList.size(); i++){
            TextView title = eliteRankList.get(i);
            assert response.body() != null;
            title.setText(response.body().get(count).getName());
            String conditions = "Need min orders: " + response.body().get(count).getMinOrders() +
                    ", also min count of comments" + response.body().get(count).getMinComments();
            TextView conditionsInfoText = eliteRankList.get(i+1);
            conditionsInfoText.setText(conditions);
            StringBuilder bonuses = new StringBuilder("This rank gives you sale " + response.body().get(count).getSaleValue() +
                    " percents on order each " + response.body().get(count).getSalePeriod() + " days, also " +
                    response.body().get(count).getFreeOrders() + " free orders on " +
                    response.body().get(count).getCarClassName() + " class each " + response.body().get(count).getPeriod() +
                    " days, ");
            for(int j = count + 1; j < response.body().size(); j++) {
                if(j > 0) {
                    if (response.body().get(j).getRanksId() > response.body().get(j - 1).getRanksId()) {
                        count = j;
                        break;
                    }
                }
                bonuses.append(response.body().get(j).getFreeOrders()).append(" free orders on ").append(response.body().get(j).getCarClassName()).
                        append(" class each ").append(response.body().get(j).getPeriod()).append(" days");
            }
            TextView bonusesInfoText = eliteRankList.get(i + 2);
            bonusesInfoText.setText(bonuses.toString());
            if(i == 6){
                break;
            }
            i = i + 2;
        }
    }

    private void textViewInitialize(){
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