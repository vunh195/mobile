package CKT.IchiCo.ShoppingNeko.firebaseviewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

public class ProductSearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String mSearchQuery;
    private String mCategoryName;

    public ProductSearchViewModelFactory(String searchQuery, String category) {
        this.mSearchQuery = searchQuery;
        this.mCategoryName = category;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductSearchViewModel.class)) {
            return (T) new ProductSearchViewModel(mSearchQuery, mCategoryName);
        } else {
            throw new IllegalArgumentException("This is unknown ViewModel class :/ ");
        }
    }
}
