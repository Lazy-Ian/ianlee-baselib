package com.ianlee.lazy.base.lib.utils.log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Android terminal log output implementation for {@link LogAdapter}.
 *
 * Prints output to LogCat with pretty borders.
 *
 * <pre>
 *  ┌──────────────────────────
 *  │ Method stack history
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Log message
 *  └──────────────────────────
 * </pre>
 */
public class AndroidLogAdapter implements LogAdapter {

  @NonNull
  private final FormatStrategy formatStrategy;

  public AndroidLogAdapter() {
    this.formatStrategy = PrettyFormatStrategy.newBuilder().build();
  }

  public AndroidLogAdapter(@NonNull FormatStrategy formatStrategy) {
    this.formatStrategy = Utils.checkNotNull(formatStrategy);
  }

  @Override public boolean isLoggable(int priority, @Nullable String tag) {
    return true;
  }

  @Override public void log(int priority, @Nullable String tag, @NonNull String message) {
    formatStrategy.log(priority, tag, message);
  }

}
