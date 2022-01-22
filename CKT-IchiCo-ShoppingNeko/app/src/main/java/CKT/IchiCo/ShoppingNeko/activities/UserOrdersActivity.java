package CKT.IchiCo.ShoppingNeko.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import CKT.IchiCo.ShoppingNeko.R;
import CKT.IchiCo.ShoppingNeko.adapters.OrdersAdapter;
import CKT.IchiCo.ShoppingNeko.firebaseviewmodel.UserOrdersViewModel;
import CKT.IchiCo.ShoppingNeko.model.Order;

public class UserOrdersActivity extends AppCompatActivity {

    private static final String LAYOUT_KEY = "elamien.abdullah.shopemall.activities";
    @BindView(R.id.userOrdersRecyclerView)
    RecyclerView mOrdersRecyclerView;
    @BindView(R.id.userOrdersToolbar)
    Toolbar mToolbar;
    @BindView(R.id.userOrdersHeaderImage)
    KenBurnsView mHeaderImage;
    @BindView(R.id.userOrdersCollapsingToolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.userOrdersEmptyState)
    View mEmptyState;
    private Parcelable mLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenWindow();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_user_orders);
        ButterKnife.bind(this);
        setupToolbar();
        loadOrders();

        if (savedInstanceState != null) {
            mLayoutState = savedInstanceState.getParcelable(LAYOUT_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLayoutState = mOrdersRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LAYOUT_KEY, mLayoutState);
    }

    private void setFullScreenWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbar.setTitle(getString(R.string.order_activity_label));
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder);

        Glide.with(this)
                .load(getString(R.string.user_orders_header_image))
                .apply(options)
                .into(mHeaderImage);
        /*
        GlideApp.with(this)
                .load(getString(R.string.user_orders_header_image))
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(mHeaderImage);

         */

    }

    private void loadOrders() {
        UserOrdersViewModel model = ViewModelProviders.of(this).get(UserOrdersViewModel.class);
        model.getUserOrders().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable List<Order> orders) {
                if (orders.size() == 0) {
                    showEmptyState();
                } else {
                    hideEmptyState();
                    OrdersAdapter adapter = new OrdersAdapter(orders, UserOrdersActivity.this);
                    mOrdersRecyclerView.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLayoutState != null) {
            mOrdersRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutState);
        }
    }

    private void showEmptyState() {
        mEmptyState.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState() {
        mEmptyState.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

        }
        return true;
    }

}
