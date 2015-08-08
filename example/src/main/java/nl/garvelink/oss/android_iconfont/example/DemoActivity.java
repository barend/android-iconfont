/**
 * Copyright 2014, Barend Garvelink
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.garvelink.oss.android_iconfont.example;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import nl.garvelink.oss.android_iconfont.IconFontDrawable;


/**
 * Sample usage of {@link nl.garvelink.oss.android_iconfont.IconFontDrawable}.
 */
public class DemoActivity extends AppCompatActivity {

    private static final boolean HAS_ANIMATOR_API = Build.VERSION.SDK_INT >= 11;

    private static Typeface fontAwesome;
    private ObjectAnimator menuItemRotator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fontAwesome == null) {
            // You shouldn't assign static fields from an instance method. Ssh!
            fontAwesome = Typeface.createFromAsset(getAssets(), "FontAwesome-4.1.0.otf");
        }
        setContentView(R.layout.activity_demo);
        int eight_dp = (int)(getResources().getDisplayMetrics().density * 8);
        int twentyfour_dp = 3 * eight_dp;

        TextView compoundDrawables = (TextView) findViewById(R.id.compound_drawables);
        //
        // * The most basic IconFontDrawable can be directly constructed.
        //
        Drawable down  = new IconFontDrawable(fontAwesome, Icons.FA_ANGLE_DOWN,  Color.BLUE, eight_dp);
        Drawable left  = new IconFontDrawable(fontAwesome, Icons.FA_ANGLE_LEFT,  Color.BLUE, eight_dp);
        Drawable right = new IconFontDrawable(fontAwesome, Icons.FA_ANGLE_RIGHT, Color.BLUE, eight_dp);
        Drawable up    = new IconFontDrawable(fontAwesome, Icons.FA_ANGLE_UP,    Color.BLUE, eight_dp);
        compoundDrawables.setCompoundDrawablesWithIntrinsicBounds(left, up, right, down);

        ImageView imageNoIntrinsic = (ImageView) findViewById(R.id.image_no_intrinsic);
        //
        // * You can use setters for the properties that are not exposed in the constructor.
        //
        IconFontDrawable image = new IconFontDrawable(fontAwesome, Icons.FA_ARROWS_ALT, Color.BLACK);
        image.setColor(getResources().getColorStateList(R.color.pressed_color_selector));
        image.setPadding(0);
        imageNoIntrinsic.setImageDrawable(image);

        //
        // * For more complex IFD's, the Builder is nicer.
        //
        ImageView imageCenter = (ImageView) findViewById(R.id.image_center);
        image = IconFontDrawable.builder(this)
                .setTypeface(fontAwesome)
                .setColorResource(android.R.color.black)
                .setGlyph(Icons.FA_PAW)
                .setIntrinsicSize(60, TypedValue.COMPLEX_UNIT_DIP)
                .build();
        imageCenter.setImageDrawable(image);

        //
        // * You'll use dp's a lot, so intrinsicSize and padding can be built directly in dp.
        //
        ImageView imageFitEnd = (ImageView) findViewById(R.id.image_fit_end);
        image = IconFontDrawable.builder(this)
                .setTypeface(fontAwesome)
                .setColorValue(Color.BLUE)
                .setGlyph(Icons.FA_FLOPPY_O)
                .setIntrinsicSizeInDip(60)
                .setPaddingInDip(0)
                .setOpacity(0.2f)
                .setRotation(-6f)
                .build();
        imageFitEnd.setImageDrawable(image);

        TextView fontAwesomeLink = (TextView) findViewById(R.id.font_awesome);
        IconFontDrawable fontAwesomeIcon = new IconFontDrawable(fontAwesome, Icons.FA_FLAG, Color.BLACK, twentyfour_dp);
        createAndStartOpacityAnimation(fontAwesomeIcon);
        fontAwesomeLink.setCompoundDrawablesWithIntrinsicBounds(fontAwesomeIcon, null, null, null);
        fontAwesomeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("http://fortawesome.github.io/Font-Awesome/");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        // * Here we create a Builder with all the common properties for actionbar icons.
        //
        IconFontDrawable.Builder icons = IconFontDrawable.builder(this)
                .setTypeface(fontAwesome)
                .setColorResource(R.color.action_bar_color_selector)
                .setIntrinsicSizeInDip(32)
                .setPaddingInDip(4);

        getMenuInflater().inflate(R.menu.demo, menu);
        MenuItem menuGithub = menu.findItem(R.id.action_github);
        MenuItem menuRotate = menu.findItem(R.id.action_rotate);

        //
        // * This single Builder can be re-used for many icons, all properties are kept.
        //
        IconFontDrawable iconGithub = icons.setGlyph(Icons.FA_GITHUB).build();
        IconFontDrawable iconRotate = icons.setGlyph(Icons.FA_CIRCLE_O_NOTCH).build();

        menuGithub.setIcon(iconGithub);
        menuRotate.setIcon(iconRotate);
        //
        // * The properties that have setters can be animated.
        //
        createRotationAnimation(iconRotate);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_rotate) {
            toggleRotationAnimation();
            return true;
        } else if (id == R.id.action_github) {
            openURL("https://github.com/barend/android-iconfont");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openURL(String url) {
        try {
            Intent www = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            www.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(www);
        } catch (Exception e) {
            // Ignored.
        }
    }

    @SuppressLint("NewApi")
    private void createAndStartOpacityAnimation(IconFontDrawable icon) {
        if (HAS_ANIMATOR_API) {
            ObjectAnimator alphaAnim = ObjectAnimator.ofInt(icon, "alpha", 0x0A, 0xFF);
            alphaAnim.setRepeatCount(ObjectAnimator.INFINITE);
            alphaAnim.setRepeatMode(ObjectAnimator.REVERSE);
            alphaAnim.setStartDelay(0L);
            alphaAnim.setDuration(2500L);
            alphaAnim.setInterpolator(new AccelerateInterpolator());
            alphaAnim.start();
        }
    }

    @SuppressLint("NewApi")
    private void createRotationAnimation(IconFontDrawable menuItemIcon) {
        if (HAS_ANIMATOR_API) {
            menuItemRotator = ObjectAnimator.ofFloat(menuItemIcon, "rotation", 0f, 359.99f);
            menuItemRotator.setRepeatCount(ObjectAnimator.INFINITE);
            menuItemRotator.setRepeatMode(ObjectAnimator.RESTART);
            menuItemRotator.setStartDelay(0L);
            menuItemRotator.setDuration(1000L);
            menuItemRotator.setInterpolator(new LinearInterpolator());
        }
    }

    @SuppressLint("NewApi")
    private void toggleRotationAnimation() {
        if (HAS_ANIMATOR_API) {
            if (menuItemRotator.isPaused()) {
                menuItemRotator.resume();
            } else if (menuItemRotator.isRunning()) {
                menuItemRotator.pause();
            } else {
                menuItemRotator.start();
            }
        } else {
            Toast.makeText(this, getString(R.string.toast_rotation), Toast.LENGTH_SHORT).show();
        }
    }
}
