package CKT.IchiCo.ShoppingNeko.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import CKT.IchiCo.ShoppingNeko.R;
import CKT.IchiCo.ShoppingNeko.adapters.ProductsAdapter;
import CKT.IchiCo.ShoppingNeko.firebaseviewmodel.CartViewModel;
import CKT.IchiCo.ShoppingNeko.firebaseviewmodel.ProductsViewModel;
import CKT.IchiCo.ShoppingNeko.firebaseviewmodel.ProductsViewModelFactory;
import CKT.IchiCo.ShoppingNeko.model.Product;
import CKT.IchiCo.ShoppingNeko.model.User;

public class CategoryProductsActivity extends AppCompatActivity {

    @BindView(R.id.productsRecyclerView)
    RecyclerView mProductsRecyclerView;
    @BindView(R.id.categoryProductsToolbar)
    Toolbar mToolbar;
    @BindView(R.id.productsSearchView)
    MaterialSearchView mSearchView;

    private TextView mCartBadgeTextView;
    private String mCategory;
    private ProductsAdapter mProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_category_products);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Intent intent = getIntent();
        setupSearchView();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mCategory = intent.getStringExtra(Intent.EXTRA_TEXT);
            setupToolbar();
            loadCategoryProducts();

        } else {
            errorUponLaunch();
        }


    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mCategory);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void loadCategoryProducts() {
        ProductsViewModelFactory factory = new ProductsViewModelFactory(mCategory);
        ProductsViewModel productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        productsViewModel.getCategoryProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                mProductsAdapter = new ProductsAdapter(CategoryProductsActivity.this, products);
                mProductsRecyclerView.setAdapter(mProductsAdapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void setupSearchView() {
        mSearchView.setVoiceSearch(true);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                launchSearchActivity(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void launchSearchActivity(String query) {
        Intent intent = new Intent(CategoryProductsActivity.this, SearchableActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, query);
        intent.putExtra(Intent.EXTRA_TITLE, mCategory);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_products_menu, menu);
        final MenuItem cartMenuItem = menu.findItem(R.id.cartMenuItemAction);
        MenuItem searchItem = menu.findItem(R.id.searchAction);
        mSearchView.setMenuItem(searchItem);

        View actionView = cartMenuItem.getActionView();
        mCartBadgeTextView = actionView.findViewById(R.id.cartMenuItemBadge);
        setupCartBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(cartMenuItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartMenuItemAction:
                launchCartActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchCartActivity() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    private void setupCartBadge() {
        CartViewModel model = ViewModelProviders.of(this).get(CartViewModel.class);
        model.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mCartBadgeTextView.setText(String.valueOf(user.getProductsCount()));
            }
        });
    }


    private void errorUponLaunch() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
        finish();
    }
}
