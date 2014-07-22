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
package nl.garvelink.oss.android_iconfont;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

/**
 * Created by barend on 9-6-14.
 */
public class IconFontDrawableTest extends InstrumentationTestCase {

    private Context context;
    private ColorStateList colorStateList;

    public void setUp() {
        context = getInstrumentation().getTargetContext();
        final int[][] states = {ViewStates.focused(), ViewStates.empty()};
        final int[] colors = {Color.YELLOW, Color.BLACK};
        colorStateList = new ColorStateList(states, colors);
    }

    @LargeTest
    public void testSimpleColorAndAlpha() {
        IconFontDrawable drawable = new IconFontDrawable(null, 'A', 0xFF112233);
        CountingDrawableCallback callback = new CountingDrawableCallback(drawable);
        drawable.setCallback(callback);
        // Check initial state.
        assertFalse(drawable.isStateful());
        assertEquals("ff112233", Integer.toHexString(drawable.getRenderingColor()));
        assertEquals(0, callback.getInvalidateCount());
        // Set alpha to new value, color is kept.
        drawable.setAlpha(0x76);
        assertEquals("76112233", Integer.toHexString(drawable.getRenderingColor()));
        assertEquals(1, callback.getInvalidateCount());
        // Set color to new value, alpha is kept.
        drawable.setColor(0xFF998877);
        assertEquals("76998877", Integer.toHexString(drawable.getRenderingColor()));
        assertEquals(2, callback.getInvalidateCount());
        // Set color to current value, callback is not invoked.
        drawable.setColor(0xFF998877);
        assertEquals("76998877", Integer.toHexString(drawable.getRenderingColor()));
        assertEquals(2, callback.getInvalidateCount());
        // Set alpha to current value, callback is not invoked.
        drawable.setAlpha(0x76);
        assertEquals("76998877", Integer.toHexString(drawable.getRenderingColor()));
        assertEquals(2, callback.getInvalidateCount());
        // Check unrelated callbacks.
        assertEquals(0, callback.getScheduleCount());
        assertEquals(0, callback.getUnscheduleCount());
    }

    @LargeTest
    public void testStatefulColorAndAlpha() {
        IconFontDrawable drawable = IconFontDrawable.builder(context)
                .setGlyph('A')
                .setColorStateList(colorStateList)
                .setIntrinsicSizeInPixels(160)
                .build();
        CountingDrawableCallback callback = new CountingDrawableCallback(drawable);
        drawable.setCallback(callback);
        assertTrue(drawable.isStateful());
        assertEquals("ff000000", Integer.toHexString(drawable.getRenderingColor()));
        assertEquals(0, callback.getInvalidateCount());
        drawable.setAlpha(0x76);
        assertEquals("76000000", Integer.toHexString(drawable.getRenderingColor()));
        assertEquals(1, callback.getInvalidateCount());
        drawable.setColor(0xFF998877);
        assertEquals("setColor() should have no effect when a stateList is present", "76000000", Integer.toHexString(drawable.getRenderingColor()));
        drawable.setState(ViewStates.focused());
        assertEquals("76ffff00", Integer.toHexString(drawable.getRenderingColor()));
        assertEquals(160, drawable.getIntrinsicHeight());
        assertEquals(160, drawable.getIntrinsicWidth());
        assertEquals(2, callback.getInvalidateCount());
        assertEquals(0, callback.getScheduleCount());
        assertEquals(0, callback.getUnscheduleCount());
    }

    /**
     * Helper class to access the protected scope view state arrays.
     */
    private static abstract class ViewStates extends View {
        private ViewStates(Context context) {
            super(context);
        }

        public static int[] focused() {
            return FOCUSED_STATE_SET;
        }

        public static int[] empty() {
            return EMPTY_STATE_SET;
        }
    }

    /**
     * Helper class to ensure in test that the callbacks are invoked.
     */
    private static class CountingDrawableCallback implements Drawable.Callback {
        private Drawable expectedCaller;
        private int invalidateCount;
        private int scheduleCount;
        private int unscheduleCount;

        private CountingDrawableCallback(Drawable expectedCaller) {
            this.expectedCaller = expectedCaller;
        }

        @Override
        public void invalidateDrawable(Drawable who) {
            if (expectedCaller != who) {
                throw new AssertionError();
            }
            invalidateCount++;
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            if (expectedCaller != who) {
                throw new AssertionError();
            }
            scheduleCount++;
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            if (expectedCaller != who) {
                throw new AssertionError();
            }
            unscheduleCount++;
        }

        public int getInvalidateCount() {
            return invalidateCount;
        }

        public int getScheduleCount() {
            return scheduleCount;
        }

        public int getUnscheduleCount() {
            return unscheduleCount;
        }
    }
}
