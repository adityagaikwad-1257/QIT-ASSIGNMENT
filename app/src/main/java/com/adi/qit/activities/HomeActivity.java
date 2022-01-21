package com.adi.qit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.adi.qit.R;
import com.adi.qit.adapters.HomeRAdapter;
import com.adi.qit.adapters.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import me.relex.circleindicator.CircleIndicator3;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager2 viewPager;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.offer_view_pager);
        viewPager.setAdapter(new ViewPagerAdapter());

        CircleIndicator3 indicator3 = findViewById(R.id.indicator);
        indicator3.setViewPager(viewPager);

        recyclerView = findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new HomeRAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out_btn)
            signOut();
        return true;
    }

    private void signOut() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
            auth.signOut();

        startActivity(new Intent(this, SignInUpActivity.class));
        finish();

    }
}