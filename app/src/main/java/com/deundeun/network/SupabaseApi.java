package com.deundeun.network;

import com.deundeun.models.Recipe;
import com.deundeun.models.Inquiry;
import com.deundeun.models.StorageCare;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface SupabaseApi {
    @GET("rest/v1/Recipe?select=*&limit=1")
    Call<List<Recipe>> getFirstRecipe();

    @GET("rest/v1/Recipe?select=*")
    Call<List<Recipe>> getRecipes();

    @GET("rest/v1/Recipe?select=*")
    Call<List<Recipe>> getRecipeById(@Query("id") String id);

    @GET("rest/v1/Recipe?select=*")
    Call<List<Recipe>> findRecipeByOptions(
        @Query("title") String title,
        @Query("CookingTools") String tools,
        @Query("ingredients") String ingredients
    );

    @GET("rest/v1/StorageCare?select=*,RecipeStorageCare!inner(recipe_id)")
    Call<List<StorageCare>> getStorageCareByRecipeId(@Query("RecipeStorageCare.recipe_id") String recipeId);

    @GET("rest/v1/Scrap?select=*")
    Call<List<com.deundeun.models.Scrap>> checkScrap(
        @Query("user_id") String userId,
        @Query("recipe_id") String recipeId
    );

    @POST("rest/v1/Scrap")
    Call<Void> addScrap(@Body com.deundeun.models.Scrap scrap);

    @retrofit2.http.DELETE("rest/v1/Scrap")
    Call<Void> removeScrap(
        @Query("user_id") String userId,
        @Query("recipe_id") String recipeId
    );

    @GET("rest/v1/Recipe?select=*,Scrap!inner(user_id)")
    Call<List<Recipe>> getScrappedRecipes(@Query("Scrap.user_id") String userId);

    @POST("rest/v1/Recipe")
    @retrofit2.http.Headers({"Prefer: resolution=merge-duplicates"})
    Call<Void> upsertRecipe(@Body Recipe recipe);

    @POST("rest/v1/Inquiry")
    Call<Void> submitInquiry(@Body Inquiry inquiry);

    @GET("rest/v1/Inquiry?select=*")
    Call<List<Inquiry>> getInquiriesByUserId(@Query("user_id") String userId);

    @GET("rest/v1/Inquiry?select=*")
    Call<List<Inquiry>> getAllInquiries();

    @GET("rest/v1/User?select=*")
    Call<List<com.deundeun.models.User>> getUserProfile(@Query("id") String userId);

    @retrofit2.http.PATCH("rest/v1/Inquiry")
    Call<Void> updateInquiry(
        @Query("id") String inquiryId,
        @Body java.util.Map<String, Object> updates
    );

    @POST("auth/v1/signup")
    Call<SignUpResponse> signUp(@Body SignUpRequest request);

    @POST("auth/v1/token?grant_type=password")
    Call<SignInResponse> signIn(@Body SignInRequest request);

    class SignUpRequest {
        public String email;
        public String password;
        public UserMetadata data;

        public SignUpRequest(String email, String password, String username, String phone) {
            this.email = email;
            this.password = password;
            this.data = new UserMetadata(username, phone);
        }

        public static class UserMetadata {
            public String username;
            public String phone;
            public UserMetadata(String username, String phone) {
                this.username = username;
                this.phone = phone;
            }
        }
    }

    class SignUpResponse {
        public String id;
        public String email;
    }

    class SignInRequest {
        public String email;
        public String password;
        public SignInRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    class SignInResponse {
        public String access_token;
        public UserResponse user;

        public static class UserResponse {
            public String id;
            public String email;
        }
    }
}
