# IconFontDrawable

Icon font support for Android.

## Status

I wrote this because I wanted to play with the relevant API's, mainly I wanted to try text scaling
using a Path. It works and the code is pretty clean, so you should be okay if you use it. I don't
plan to actively maintain this library.

## Why use an icon font?

You can use an icon font instead of PNG assets if you want to support all pixel densities without
exporting all the slices. You may also take the opportunity to create a single icon font for your
website and other platforms' apps.

Compared to manually sliced PNG assets, the price you pay is two-fold: you can only use a single
color and you lose some control over the anti-aliasing. You'll have to see for yourself if this is
problematic in your icon set and design.

## Where can I get an icon font?

There are a lots of online sites to help you here. The example app in this project uses icons from
the [FontAwesome][ftaw] project. I've used [icomoon.io][icom] in the past. A web search will turn
up tons more options.

[ftaw]: https://fortawesome.github.io/Font-Awesome/
[icom]: http://icomoon.io/

## Usage

Instances of `IconFontDrawable` are created in Java, usually during `onCreate()`. They need a color,
and a `Typeface` and `char` to select which glyph to use. You can choose to set the `intrinsicSize`,
which controls the size of the icon drawable. If an intrinsic size is set, the drawable will behave
like `BitmapDrawable`. If no intrinsic size is set, the drawable will fill whatever space its bounds
are set to. In some cases, this will be zero.

If the assigned bounds is not square, the glyph will max out the shorter axis and center in the
longer axis. It is possible to assign a `padding` to ensure some space around the glyph.

## Sample code

Check out the example project's `DemoActivity` for a number of different uses. In short, you'll use
either the constructor and setters, or the provided builder class.

Using the constructor and setters is not the preferred way:

```java
    Typeface deviceDefault = null;

    IconFontDrawable icon = new IconFontDrawable(deviceDefault, 'A', Color.BLACK);
    icon.setIntrinsicSize(40); // px
    icon.setPadding(4);        // px
    icon.setRotation(90f);     // degrees
```

The builder makes for nicer code:

```java
    //
    // * Here we create a Builder with all the common properties for actionbar icons.
    //
    IconFontDrawable.Builder icons = IconFontDrawable.builder(this)
            .setTypeface(fontAwesome)
            .setColorResource(R.color.action_bar_color_selector)
            .setIntrinsicSizeInDip(32)
            .setPaddingInDip(4);
    //
    // * A single Builder can be re-used for many icons, all properties are kept.
    //
    IconFontDrawable iconEmpire = icons.setGlyph(Icons.FA_EMPIRE).build();
    IconFontDrawable iconRebels = icons.setGlyph(Icons.FA_REBEL ).build();
```

Calling any of the setters will invalidate the drawable, therefore they can be used for animation:

```java
     ObjectAnimator rotator = ObjectAnimator.ofFloat(iconEmpire, "rotation", 0f, 359.99f);
     rotator.setRepeatCount(ObjectAnimator.INFINITE);
     rotator.setRepeatMode(ObjectAnimator.RESTART);
     rotator.setStartDelay(0L);
     rotator.setDuration(1000L);
     rotator.setInterpolator(new LinearInterpolator());
     rotator.start();
```

## Sample app

A small sample app is included. It's not in the Play Store, but here's a screenshot. You can click
it for the full size version.

[![Screenshot][thumb]][large]

[thumb]: https://raw.githubusercontent.com/barend/android-iconfont/master/example/Font-Icon-Demo_framed-small.png
[large]: https://raw.githubusercontent.com/barend/android-iconfont/master/example/Font-Icon-Demo_framed.png

# Alternatives

For your own projects, you may prefer the ShamanLand.Com FontIcon library ([GitHub][SLGH],
[Blog][SLBL]). It's been around for a while longer, so more people have tested it. The main
differences between ShamanLand.Com's library and this one at the time of writing:

ShamanLand                             | this one
---------------------------------------|--------------------------------------------------
Icons defined in XML or Java           | Icons only defined in Java
Glyphs rendered at a given text size   | Glyphs auto-scaled to fit bounds
Includes custom Views for XML layout   | nope
Includes typeface cache                | nope
Supports auto-mirroring for RTL        | nope
Implements Parcelable for state saving | nope
Receives frequent updates              | nope

[SLGH]: https://github.com/shamanland/fonticon
[SLBL]: http://blog.shamanland.com/p/android-fonticon-library.html

# Copyright and License

```text
Copyright 2014, Barend Garvelink

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

