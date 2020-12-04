package ua.shishkoam.createcourse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

public class PinView extends SubsamplingScaleImageView {

    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();
    private PointF sPin;
    private Bitmap pin;
    private ArrayList<PointF> savedPin = new ArrayList<>();

    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        initialise();
        invalidate();
    }

    public void clearPin() {
        savedPin.clear();
    }

    public void savePin() {
        if (sPin != null) {
            savedPin.add(sPin);
        }
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.circle_8);
        float w = (density / 420f) * pin.getWidth();
        float h = (density / 420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int) w, (int) h, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        paint.setAntiAlias(true);

        for (PointF point : savedPin) {
            addMarker(canvas, point);
        }
        if (sPin != null && pin != null) {
            addMarker(canvas, sPin);
        }

    }

    private void addMarker(Canvas canvas, PointF sPin) {
        sourceToViewCoord(sPin, vPin);
        float vX = vPin.x - (pin.getWidth() / 2);
        float vY = vPin.y - (pin.getHeight() / 2);
        canvas.drawBitmap(pin, vX, vY, paint);
    }


}
