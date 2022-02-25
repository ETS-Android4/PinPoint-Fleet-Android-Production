package slider;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.fleetanalytics.pinpoint.R;

public class SegmentedRadioGroup extends RadioGroup {

	public SegmentedRadioGroup(Context context) {
		super(context);
	}

	public SegmentedRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			changeButtonsImages();
		}
		
	}

	//@SuppressLint("NewApi")
	private void changeButtonsImages() {
		int count = super.getChildCount();

		if (count > 1) {
			super.getChildAt(0).setBackground(
					getResources().getDrawable(R.drawable.segment_radio_left));
			for (int i = 1; i < count - 1; i++) {
				super.getChildAt(i).setBackground(
						getResources().getDrawable(
								R.drawable.segment_radio_middle));
			}
			super.getChildAt(count - 1).setBackground(
					getResources().getDrawable(R.drawable.segment_radio_right));
		} else if (count == 1) {
			super.getChildAt(0).setBackground(
					getResources().getDrawable(R.drawable.segment_button));
			// super.getChildAt(0).setBackgroundResource(com.makeramen.segmented.R.drawable.segment_button);
		}
	}
}