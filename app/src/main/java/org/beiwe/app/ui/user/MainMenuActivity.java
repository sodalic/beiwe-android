package org.beiwe.app.ui.user;

import java.util.*;

import io.sodalic.blob.R;
import io.sodalic.blob.storage.UserMood;
import io.sodalic.blob.utils.StringUtils;
import org.beiwe.app.session.SessionActivity;
import org.beiwe.app.storage.PersistentData;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The main menu activity of the app. Currently displays 4 buttons - Audio Recording, Graph, Call Clinician, and Sign out.
 *
 * @author Dor Samet
 */
public class MainMenuActivity extends SessionActivity {

    private ImageView moodImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView titleView = findViewById(R.id.main_menu_title);
        titleView.setText(getString(R.string.main_menu_top_message_format_1, getBlobContext().getUserStateData().getUserName()));

        moodImageView = findViewById(R.id.mood_image_view);

        Button callClinicianButton = (Button) findViewById(R.id.main_menu_call_clinician);
        if (PersistentData.getCallClinicianButtonEnabled()) {
            callClinicianButton.setText(PersistentData.getCallClinicianButtonText());
        } else {
            callClinicianButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserMood lastMood = getBlobContext().getUserStateData().getLastMood();
        Log.i(logTag, StringUtils.formatEn("Showing UI for mood %s", lastMood));
        Drawable moodImage = getMoodImage(lastMood);
        moodImageView.setImageDrawable(moodImage);
    }

    private Drawable getMoodImage(UserMood mood) {
        int[][] keysAndValues = new int[][]{
                new int[]{R.drawable.mood_positive, mood.happiness},
                new int[]{R.drawable.mood_negative, mood.sadness},
                new int[]{R.drawable.mood_anger, mood.anger},
                new int[]{R.drawable.mood_fear, mood.fear}
        };
        // sort to find the mood with the max value
        Arrays.sort(keysAndValues, new ByValueComparator());
        int[] maxValue = keysAndValues[0];
        return getDrawable(maxValue[0]);
    }

    static class ByValueComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] kv1, int[] kv2) {
            return -Integer.compare(kv1[1], kv2[1]);
        }
    }

	/*#########################################################################
	############################## Buttons ####################################
	#########################################################################*/

//	public void graphResults (View v) { startActivity( new Intent(getApplicationContext(), GraphActivity.class) ); }
}
