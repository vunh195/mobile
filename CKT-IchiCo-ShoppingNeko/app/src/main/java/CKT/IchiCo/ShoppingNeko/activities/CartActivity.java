package CKT.IchiCo.ShoppingNeko.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;
import com.tapadoo.alerter.Alerter;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import CKT.IchiCo.ShoppingNeko.R;
import CKT.IchiCo.ShoppingNeko.adapters.CartAdapter;
import CKT.IchiCo.ShoppingNeko.model.Product;
import CKT.IchiCo.ShoppingNeko.model.User;
import CKT.IchiCo.ShoppingNeko.utils.Constants;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


public class CartActivity extends AppCompatActivity implements CartAdapter.CartPriceListener {

    @BindView(R.id.categoryCartProductsRecyclerView)
    RecyclerView mCartProductsRecyclerView;
    @BindView(R.id.cartProductEmptyState)
    View mCartEmptyState;
    @BindView(R.id.totalPriceTextView)
    TextView mTotalPriceTextView;
    @BindView(R.id.cartActivityParent)
    ViewGroup mCartActivityParent;
    @BindView(R.id.orderButton)
    Button mOrderButton;
    @BindView(R.id.cartToolbar)
    Toolbar mToolbar;
    @BindView(R.id.headerImage)
    KenBurnsView mHeaderImage;
    @BindView(R.id.cartCollapsingToolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.retryConnectionButton)
    Button mRetryConnectionButton;
    private CartAdapter mCartAdapter;
    private DatabaseReference mReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenWindow();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        if (this.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE && findViewById(R.id.cartParentTablet) == null) {
            changeRecyclerViewLayoutManager();
        }
        setupToolbar();
        setupViews();
    }

    private void setupViews() {
        if (isNetworkAvailable()) {
            hideRetryButton();
            setupDatabase();
            setupCartRecyclerView();
        } else {
            hideEmptyState();
            showRetryButton();
            connectionErrorMessage();
        }
        this.onCartPriceChangeListener("");
    }

    @OnClick(R.id.retryConnectionButton)
    public void onRetryButtonClick() {
        setupViews();
    }

    private void hideEmptyState() {
        mCartEmptyState.setVisibility(View.GONE);
        mOrderButton.setVisibility(View.GONE);
    }

    private void hideRetryButton() {
        mRetryConnectionButton.setVisibility(View.GONE);
    }

    private void showRetryButton() {
        mRetryConnectionButton.setVisibility(View.VISIBLE);
    }

    private void connectionErrorMessage() {
        Alerter.create(this)
                .setTitle(R.string.connection_alerter_title)
                .setText(R.string.connection_alerter_text)
                .setIcon(R.drawable.ic_network)
                .setDuration(getResources().getInteger(R.integer.alerter_default_duration))
                .setDismissable(true)
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void changeRecyclerViewLayoutManager() {
        mCartProductsRecyclerView.setLayoutManager(new CardSliderLayoutManager(12, 1010, 12));
        new CardSnapHelper().attachToRecyclerView(mCartProductsRecyclerView);
    }

    private void setFullScreenWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        String cartTitle = getString(R.string.cart_activity_label);
        mCollapsingToolbar.setTitle(cartTitle);
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder);

        Glide.with(this)
                .load(getString(R.string.cart_header_image))
                .apply(options)
                .into(mHeaderImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupCartRecyclerView() {
        mCartAdapter = new CartAdapter(new ArrayList<Product>(), this
                , this);
        mCartProductsRecyclerView.setAdapter(mCartAdapter);
    }

    private String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void setupDatabase() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child(Constants.USER_CHILD);
        loadCartProducts();
    }

    private void loadCartProducts() {
        mReference.orderByChild(Constants.USER_ID_CHILD).equalTo(getUserId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                mCartAdapter.addCartProducts(user.getCart().getCartProducts(), dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hideRecyclerView() {
        mCartProductsRecyclerView.setVisibility(View.GONE);
        mCartEmptyState.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mCartEmptyState.setVisibility(View.GONE);
        mCartProductsRecyclerView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.orderButton)
    public void onOrderButtonClick() {
        startActivity(new Intent(this, OrderActivity.class));
    }

    @Override
    public void onCartPriceChangeListener(String price) {
        if (price.equals("")) {
            mOrderButton.setVisibility(View.GONE);
            mTotalPriceTextView.setText("");
            if (isNetworkAvailable()) hideRecyclerView();
            return;
        }
        mOrderButton.setVisibility(View.VISIBLE);
        showRecyclerView();
        String fullText = getString(R.string.label_total_price_cart) + " $" + price;
        TransitionManager.beginDelayedTransition(mCartActivityParent,
                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        mTotalPriceTextView.setText(fullText);
    }

}
