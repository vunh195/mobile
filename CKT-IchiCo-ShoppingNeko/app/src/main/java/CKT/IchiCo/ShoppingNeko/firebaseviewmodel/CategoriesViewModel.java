package CKT.IchiCo.ShoppingNeko.firebaseviewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import CKT.IchiCo.ShoppingNeko.model.Categories;
import CKT.IchiCo.ShoppingNeko.utils.Constants;


public class CategoriesViewModel extends ViewModel {
    private MutableLiveData<List<Categories>> mCategoriesList;

    public LiveData<List<Categories>> getCategories() {
        if (mCategoriesList == null) {
            mCategoriesList = new MutableLiveData<>();
            loadCategories();
        }
        return mCategoriesList;
    }

    private void loadCategories() {
        final List<Categories> categoriesList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child(Constants.CATEGORY_CHILD);
        reference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Categories category = dataSnapshot.getValue(Categories.class);
                categoriesList.add(category);
                mCategoriesList.setValue(categoriesList);
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
}
