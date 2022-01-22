package CKT.IchiCo.ShoppingNeko.wishlistviewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import java.util.List;

import CKT.IchiCo.ShoppingNeko.database.WishlistDatabase;
import CKT.IchiCo.ShoppingNeko.model.Product;

public class WishlistProductsViewModel extends AndroidViewModel {
    private LiveData<List<Product>> mWishlistedProducts;

    public WishlistProductsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Product>> getWishlistedProducts() {
        if (mWishlistedProducts == null) {
            mWishlistedProducts = new MutableLiveData<>();
            loadWishlistedProducts();
        }
        return mWishlistedProducts;
    }

    private void loadWishlistedProducts() {
        WishlistDatabase database = WishlistDatabase.getDatabase(getApplication());
        mWishlistedProducts = database.mWishListProductsDao().getAllProducts();
    }
}
