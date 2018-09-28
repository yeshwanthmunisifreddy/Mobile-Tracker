package technology.nine.mobile_tracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;

import java.util.List;
import java.util.Objects;

import io.reactivex.functions.Consumer;

public class PatternLockActivity extends AppCompatActivity {
    private PatternLockView mPatternLockView;
    TextView patternText;
    TextView clearTextView;
    Button nextBtn;
    static String pattern;
    SharedPreferences sharedPreferences;
    List<PatternLockView.Dot> patternLock;
    boolean value;
    SharedPreferences.Editor editor = null;
    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            //   Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            //  Log.d(getClass().getName(), "Pattern progress: " +
            //  PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.e(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
            patternLock = pattern;
            Log.e("Size",patternLock.size()+"");
            if (patternLock.size() > 3){
                if (!pattern.isEmpty()) {
                    clearTextView.setVisibility(View.VISIBLE);
                    nextBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }else {
                patternText.setText(R.string.connect_at_four_dots);
                patternText.setTextColor(getResources().getColor(R.color.red));
                clearTextView.setVisibility(View.VISIBLE);
                nextBtn.setBackgroundColor(getResources().getColor(R.color.mine_shaft));
            }


        }

        @Override
        public void onCleared() {
            //  Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock);
        mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);
        patternText = findViewById(R.id.pattern);
        clearTextView = findViewById(R.id.clear);
        nextBtn = findViewById(R.id.next);
        clearTextView.setVisibility(View.GONE);
        sharedPreferences = getApplicationContext().getSharedPreferences("LockPattern", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        value = sharedPreferences.getBoolean("Boolean", false);
        if (value) {
            patternText.setText(R.string.draw_an_unlock_pattern);
            nextBtn.setText(R.string.next);
        }
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
        //show back navigation
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //pattern lock value
        pattern = PatternLockUtils.patternToString(mPatternLockView, patternLock);
        clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onClear();

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnNext();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private  void onClear(){

        if (!value){
            mPatternLockView.clearPattern();
            clearTextView.setVisibility(View.GONE);
            patternText.setText(R.string.draw_an_unlock_pattern);
            nextBtn.setText(R.string.next);
            patternText.setTextColor(getResources().getColor(R.color.white));
            nextBtn.setBackgroundColor(getResources().getColor(R.color.white));
            editor.putBoolean("Boolean", false);
            editor.apply();
        }else {
            mPatternLockView.clearPattern();
            clearTextView.setVisibility(View.GONE);
            patternText.setText(R.string.draw_pattern_again_to_confirm);
            nextBtn.setText(R.string.confirm);
            patternText.setTextColor(getResources().getColor(R.color.white));
            nextBtn.setBackgroundColor(getResources().getColor(R.color.white));
            editor.putBoolean("Boolean", false);
            editor.apply();
        }

    }
    private  void OnNext(){
        mPatternLockView.clearPattern();
        clearTextView.setVisibility(View.GONE);
        patternText.setText(R.string.draw_pattern_again_to_confirm);
        nextBtn.setText(R.string.confirm);
        patternText.setTextColor(getResources().getColor(R.color.white));
        nextBtn.setBackgroundColor(getResources().getColor(R.color.mine_shaft));
        editor.putBoolean("Boolean", true);
        editor.apply();
    }


}
