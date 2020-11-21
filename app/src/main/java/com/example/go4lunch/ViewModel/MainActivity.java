package com.example.go4lunch.ViewModel;


import android.content.Intent;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Model.Users.User;
import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;

import com.firebase.ui.auth.AuthUI;

import com.google.firebase.auth.FirebaseAuth;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;




import java.util.Arrays;


public class MainActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 122;
    private static final String TWITTER_KEY = BuildConfig.TwitterKey;
    private static final String TWITTER_SECRET = BuildConfig.TwitterSecret;
    private String userId;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
        setContentView(R.layout.activity_main);
         loginBtn = findViewById(R.id.ConnexionButton);


        if(FirebaseAuth.getInstance().getCurrentUser() == null){
          loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setTheme(R.style.LoginTheme)
                                .setAvailableProviders(
                                        Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build(),
                                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                                .setIsSmartLockEnabled(false, true)
                                .setLogo(R.drawable.ic_person_black_24dp)
                                .build(),
                        RC_SIGN_IN);
            }

        });}


       // try {
       //     PackageInfo info = getPackageManager().getPackageInfo(
               //     "com.example.go4lunch",
              //      PackageManager.GET_SIGNATURES);
           // for (Signature signature : info.signatures) {
          //      MessageDigest md = MessageDigest.getInstance("SHA");
          //      md.update(signature.toByteArray());
         //       Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
     //       }
   //     } catch (PackageManager.NameNotFoundException e) {

    //    } catch (NoSuchAlgorithmException e) {

      //  }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

                loginBtn.setText(getResources().getString(R.string.resume));
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startHomeActivity();
                    }
                });
            }

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                startHomeActivity();
            }
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void createUserInFirestore() {


        userId = UserHelper.getCurrentUserId();
        String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
        String username = this.getCurrentUser().getDisplayName();
        String uid = this.getCurrentUser().getUid();


        UserHelper.getUser(userId).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d("TAG", "onSuccess: documentSnapshot exists 2 ");
                User currentUser = documentSnapshot.toObject(User.class);
                String restoname = currentUser.getRestoName();
                String restoId = currentUser.getRestoId();
                if (!(restoname.equals("") && restoId.equals(""))) {
                    UserHelper.updateRestoName(restoname, userId);
                    Log.d("TAG", "update resto name : " + restoname);
                    UserHelper.updateRestoId(restoId, userId);
                }
            } else {
                UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(this.onFailureListener());
            }
        });
    }
}




