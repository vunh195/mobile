// Generated code from Butter Knife. Do not modify!
package CKT.IchiCo.ShoppingNeko.activities;

import CKT.IchiCo.ShoppingNeko.R;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.flaviofaria.kenburnsview.KenBurnsView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RegisterActivity_ViewBinding implements Unbinder {
  private RegisterActivity target;

  private View view7f0a022f;

  private View view7f0a0230;

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegisterActivity_ViewBinding(final RegisterActivity target, View source) {
    this.target = target;

    View view;
    target.existedMembershipLabel = Utils.findRequiredViewAsType(source, R.id.loginMemberLabelTextView, "field 'existedMembershipLabel'", TextView.class);
    target.registerLayoutParent = Utils.findRequiredViewAsType(source, R.id.registerLayoutParent, "field 'registerLayoutParent'", ConstraintLayout.class);
    target.registerImage = Utils.findRequiredViewAsType(source, R.id.registerImage, "field 'registerImage'", KenBurnsView.class);
    target.registerEmailEditText = Utils.findRequiredViewAsType(source, R.id.registerEmailEditText, "field 'registerEmailEditText'", EditText.class);
    target.registerPasswordEditText = Utils.findRequiredViewAsType(source, R.id.registerPasswordEditText, "field 'registerPasswordEditText'", EditText.class);
    view = Utils.findRequiredView(source, R.id.signinWithGoogleImageView, "method 'onSigninGoogleClick'");
    view7f0a022f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSigninGoogleClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.signupButton, "method 'onSignupButtonClick'");
    view7f0a0230 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSignupButtonClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RegisterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.existedMembershipLabel = null;
    target.registerLayoutParent = null;
    target.registerImage = null;
    target.registerEmailEditText = null;
    target.registerPasswordEditText = null;

    view7f0a022f.setOnClickListener(null);
    view7f0a022f = null;
    view7f0a0230.setOnClickListener(null);
    view7f0a0230 = null;
  }
}
