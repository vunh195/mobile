package CKT.IchiCo.ShoppingNeko.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import CKT.IchiCo.ShoppingNeko.R;
import CKT.IchiCo.ShoppingNeko.fragments.WishlistFragment;

public class WishlistProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_products);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.wishlist_activity_label);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wishlistedProductsFragmentContainer, WishlistFragment.getInstance())
                    .commit();
        }
    }
}
