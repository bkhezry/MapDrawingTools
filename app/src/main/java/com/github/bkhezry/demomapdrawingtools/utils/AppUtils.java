package com.github.bkhezry.demomapdrawingtools.utils;


import android.annotation.SuppressLint;
import android.os.Build;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class AppUtils {
  @SuppressLint("ClickableViewAccessibility")
  public static void setTextWithLinks(TextView textView, CharSequence html) {
    textView.setText(html);
    textView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP ||
            action == MotionEvent.ACTION_DOWN) {
          int x = (int) event.getX();
          int y = (int) event.getY();

          TextView widget = (TextView) v;
          x -= widget.getTotalPaddingLeft();
          y -= widget.getTotalPaddingTop();

          x += widget.getScrollX();
          y += widget.getScrollY();

          Layout layout = widget.getLayout();
          int line = layout.getLineForVertical(y);
          int off = layout.getOffsetForHorizontal(line, x);

          ClickableSpan[] link = Spannable.Factory.getInstance()
              .newSpannable(widget.getText())
              .getSpans(off, off, ClickableSpan.class);

          if (link.length != 0) {
            if (action == MotionEvent.ACTION_UP) {
              link[0].onClick(widget);
            }
            return true;
          }
        }
        return false;
      }
    });
  }

  public static CharSequence fromHtml(String htmlText) {
    return fromHtml(htmlText, false);
  }

  public static CharSequence fromHtml(String htmlText, boolean compact) {
    if (TextUtils.isEmpty(htmlText)) {
      return null;
    }
    CharSequence spanned;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      spanned = Html.fromHtml(htmlText, compact ?
          Html.FROM_HTML_MODE_COMPACT : Html.FROM_HTML_MODE_LEGACY);
    } else {
      //noinspection deprecation
      spanned = Html.fromHtml(htmlText);
    }
    return trim(spanned);
  }

  private static CharSequence trim(CharSequence charSequence) {
    if (TextUtils.isEmpty(charSequence)) {
      return charSequence;
    }
    int end = charSequence.length() - 1;
    while (Character.isWhitespace(charSequence.charAt(end))) {
      end--;
    }
    return charSequence.subSequence(0, end + 1);
  }
}
