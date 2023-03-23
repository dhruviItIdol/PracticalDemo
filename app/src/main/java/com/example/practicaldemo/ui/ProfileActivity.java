package com.example.practicaldemo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.practicaldemo.R;
import com.example.practicaldemo.databinding.ActivityProfileBinding;
import com.example.practicaldemo.databinding.ActivitySignupBinding;
import com.example.practicaldemo.model.LocationData;
import com.example.practicaldemo.model.UserInfo;
import com.example.practicaldemo.model.WeatherData;
import com.example.practicaldemo.network.API_Interface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    Gson convert = new GsonBuilder().setPrettyPrinting().create();
    Retrofit callapi = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/").addConverterFactory(GsonConverterFactory.create(convert)).build();
    API_Interface i = callapi.create(API_Interface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.mainView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserInfo");
        getdata();
        binding.btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GET_LOCATION(binding.etCity.getText().toString());
            }
        });

    }
    private void getdata() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo value = snapshot.getValue(UserInfo.class);
                Log.e("Value is", String.valueOf(value));
                  binding.txtBio.setText(value.getUserBio());
                binding.txtUsername.setText(value.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("documentImages").child("noplateImg");//reach out to your photo file hierarchically
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("URI", uri.toString()); //check path is correct or not ?
                Glide.with(getBaseContext()).load(uri.toString()).into(binding.profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle errors
            }
        });

    }
    private void GET_LOCATION(String Location){
        try {
            Call<List<LocationData>> call = i.getlocationdata(
                    Location
            );

            call.enqueue(new Callback<List<LocationData>>() {
                             @Override
                             public void onResponse(Call<List<LocationData>> call, retrofit2.Response<List<LocationData>> response) {
                                 List<LocationData> locationData = response.body();
                                 if (locationData.isEmpty()){
                                     Toast.makeText(getApplicationContext(), "Please enter location", Toast.LENGTH_SHORT).show();
                                 }
                                 else {
                                     API_CALL(locationData.get(0).getLat(),locationData.get(0).getLon());
                                 }
                             }

                             @Override
                             public void onFailure(Call<List<LocationData>> call, Throwable t) {
                                 Toast.makeText(getApplicationContext(), "error"+t.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         }
            );
        }
        catch (Exception e){

        }
    }


    private void API_CALL(double lat, double lon) {
        try {

            Call<WeatherData> call = i.getweatherdata(
                    lat,lon,"4066121af6e250a7d9912845e19db5e2","metric"
            );
            call.enqueue(new Callback<WeatherData>() {
                @Override
                public void onResponse(Call<WeatherData> call, retrofit2.Response<WeatherData> response) {
                    WeatherData data = response.body();
                    binding.temp.setText(data.getMain().getTemp()+"°");
                    binding.txtWeather.setText("\nFeels like: "+data.getMain().getFeelsLike()+"°\n"+"\nHumidity: "
                            +data.getMain().getHumidity()+"%\n"+"\nMin: "+data.getMain().getTempMin()+"°      Max: "
                            +data.getMain().getTempMax()+"°");
                }

                @Override
                public void onFailure(Call<WeatherData> call, Throwable t) {

                }
            });

        }
        catch (Exception e){

        }
    }

}