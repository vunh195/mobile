package CKT.IchiCo.ShoppingNeko.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import CKT.IchiCo.ShoppingNeko.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final String WEB_API_KEY = "AIzaSyB9r5W5Em98IvKwUb1h1HyDd4OJSxJgvKs";
    private static final int RC_GOOGLE_SIGNIN = 4;

    @BindView(R.id.loginMemberLabelTextView)
    TextView existedMembershipLabel;
    @BindView(R.id.registerLayoutParent)
    ConstraintLayout registerLayoutParent;
    @BindView(R.id.registerImage)
    KenBurnsView registerImage;
    @BindView(R.id.registerEmailEditText)
    EditText registerEmailEditText;
    @BindView(R.id.registerPasswordEditText)
    EditText registerPasswordEditText;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenWindow();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        loadRegisterImage();
        setupExistedMembershipLabel();
        setupGoogleSignin();
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupGoogleSignin() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_API_KEY)
                .requestEmail()
                .build();
        mGoogleClient = GoogleSignIn.getClient(this, signInOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            launchMainActivity();
        }
    }

    private void setFullScreenWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void loadRegisterImage() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder);

        Glide.with(this)
                .load("https://drive.google.com/uc?export=download&id=1vYCO90wcu80pnZ57VrGGQRT0mfz8jDxC")
                .apply(options)
                .into(registerImage);
/*
        GlideApp.with(this)
                .load(getString(R.string.register_activity_image))
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(registerImage);
*/
    }

    private void setupExistedMembershipLabel() {
        SpannableString textLink = new SpannableString(getString(R.string.membership_register_label));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                launchLoginActivity();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        textLink.setSpan(clickableSpan, 17, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        existedMembershipLabel.setText(textLink);
        existedMembershipLabel.setMovementMethod(LinkMovementMethod.getInstance());
        existedMembershipLabel.setHighlightColor(Color.TRANSPARENT);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isTextEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private boolean isEmail(EditText emailET) {
        String email = emailET.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @OnClick(R.id.signinWithGoogleImageView)
    public void onSigninGoogleClick() {
        Intent intent = mGoogleClient.getSignInIntent();
        startActivityForResult(intent, RC_GOOGLE_SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGNIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                addUserToFirebase(account);
            } catch (ApiException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    private void addUserToFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            launchMainActivity();
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    @OnClick(R.id.signupButton)
    public void onSignupButtonClick() {
        if (!isEmail(registerEmailEditText)) {
            registerEmailEditText.setError(getString(R.string.register_email_error_msg));
        } else if (isTextEmpty(registerPasswordEditText)) {
            registerPasswordEditText.setError(getString(R.string.register_password_error_msg));
        } else {
            registerNewUser();
        }
    }

    private void registerNewUser() {
        String email = registerEmailEditText.getText().toString().trim();
        String password = registerPasswordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        launchMainActivity();
                    } else {
                        Log.d(TAG, task.getException().getMessage());
                    }
                });
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
