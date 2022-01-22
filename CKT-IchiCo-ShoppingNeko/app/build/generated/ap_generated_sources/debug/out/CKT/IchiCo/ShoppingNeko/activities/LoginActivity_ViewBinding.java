// Generated code from Butter Knife. Do not modify!
package CKT.IchiCo.ShoppingNeko.activities;

import CKT.IchiCo.ShoppingNeko.R;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.flaviofaria.kenburnsview.KenBurnsView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view7f0a018a;

  private View view7f0a0188;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    target.loginImage = Utils.findRequiredViewAsType(source, R.id.loginImage, "field 'loginImage'", KenBurnsView.class);
    target.loginEmailEditText = Utils.findRequiredViewAsType(source, R.id.loginEmailEditText, "field 'loginEmailEditText'", EditText.class);
    target.loginPasswordEditText = Utils.findRequiredViewAsType(source, R.id.loginPasswordEditText, "field 'loginPasswordEditText'", EditText.class);
    view = Utils.findRequiredView(source, R.id.loginGoogleAuthImageButton, "method 'onGoogleButtonClick'");
    view7f0a018a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onGoogleButtonClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.loginButton, "method 'onLoginButtonClick'");
    view7f0a0188 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onLoginButtonClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.loginImage = null;
    target.loginEmailEditText = null;
    target.loginPasswordEditText = null;

    view7f0a018a.setOnClickListener(null);
    view7f0a018a = null;
    view7f0a0188.setOnClickListener(null);
    view7f0a0188 = null;
  }
}
