package com.deundeun.models;

import android.util.Log;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User {
    private static final String TAG = "UserModel";
    private String id;
    private String username;
    private String email;
    private String password;
    private String phone;
    
    @com.google.gson.annotations.SerializedName("user_role")
    private String userRole;

    public static User currentUser;

    public interface AuthCallback {
        void onSuccess(User user);
        void onFailure(String message);
    }

    private transient AuthCallback callback;

    public void setAuthCallback(AuthCallback callback) {
        this.callback = callback;
    }

    public User() {}

    public User(String id, String username, String email, String password, String phone, String userRole) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userRole = userRole;
    }

    public void login() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        
        String loginEmail;
        if (this.username != null) {
            if (this.username.contains("@")) {
                loginEmail = this.username;
            } else {
                loginEmail = this.username + "@deundeun.internal";
            }
        } else {
            loginEmail = this.email;
        }

        SupabaseApi.SignInRequest request = new SupabaseApi.SignInRequest(loginEmail, this.password);
        Log.d(TAG, "Attempting login for email: " + loginEmail);
        
        api.signIn(request).enqueue(new Callback<SupabaseApi.SignInResponse>() {
            @Override
            public void onResponse(Call<SupabaseApi.SignInResponse> call, Response<SupabaseApi.SignInResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SupabaseApi.SignInResponse body = response.body();
                    String userId = body.user.id;

                    Log.d(TAG, "Auth success, UUID: " + userId);
                    api.getUserProfile("eq." + userId).enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> profileResponse) {
                            if (profileResponse.isSuccessful() && profileResponse.body() != null && !profileResponse.body().isEmpty()) {
                                User profile = profileResponse.body().get(0);
                                Log.d(TAG, "Profile found. Role: " + profile.getUserRole());
                                
                                User.this.id = userId;
                                User.this.email = body.user.email;
                                User.this.userRole = profile.getUserRole();
                                User.this.username = profile.getUsername();
                                User.this.phone = profile.getPhone();

                                User.currentUser = User.this;

                                if (callback != null) {
                                    callback.onSuccess(User.this);
                                }
                            } else {
                                Log.e(TAG, "Profile not found in 'User' table for UUID: " + userId);
                                if (profileResponse.errorBody() != null) {
                                    try { Log.e(TAG, "Profile error: " + profileResponse.errorBody().string()); } catch (Exception e) {}
                                }

                                User.this.id = userId;
                                User.this.email = body.user.email;
                                User.this.userRole = "User";

                                User.currentUser = User.this;
                                if (callback != null) callback.onSuccess(User.this);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            if (callback != null) callback.onFailure("사용자 정보를 불러오지 못했습니다.");
                        }
                    });
                } else {
                    if (callback != null) {
                        String errorMsg = "아이디 또는 비밀번호가 틀렸습니다";
                        try {
                            if (response.errorBody() != null) {
                                String bodyStr = response.errorBody().string();
                                Log.e(TAG, "Login error body: " + bodyStr);
                                if (bodyStr.contains("Email not confirmed")) {
                                    errorMsg = "이메일 인증이 완료되지 않았습니다. Supabase 설정에서 Confirm Email을 꺼주세요.";
                                } else if (bodyStr.contains("Invalid login credentials")) {
                                    errorMsg = "아이디 또는 비밀번호가 틀렸습니다. (Authentication 메뉴에서 계정을 생성했는지 확인하세요)";
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error body", e);
                        }
                        callback.onFailure(errorMsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<SupabaseApi.SignInResponse> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                if (callback != null) {
                    callback.onFailure("네트워크 오류가 발생했습니다");
                }
            }
        });
    }

    public void logout() {
        User.currentUser = null;
    }

    public void register() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        String internalEmail = (this.username != null) ? this.username + "@deundeun.internal" : this.email;
        SupabaseApi.SignUpRequest request = new SupabaseApi.SignUpRequest(internalEmail, this.password, this.username, this.phone);

        api.signUp(request).enqueue(new Callback<SupabaseApi.SignUpResponse>() {
            @Override
            public void onResponse(Call<SupabaseApi.SignUpResponse> call, Response<SupabaseApi.SignUpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (callback != null) {
                        callback.onSuccess(User.this);
                    }
                } else {
                    String errorMsg = "회원가입 실패";
                    try {
                        if (response.errorBody() != null) {
                            String body = response.errorBody().string();
                            Log.e(TAG, "Register error: " + body);
                            if (body.contains("email")) {
                                errorMsg = "이미 사용 중인 아이디입니다";
                            } else if (body.contains("phone")) {
                                errorMsg = "이미 사용 중인 전화번호입니다";
                            }
                        }
                    } catch (Exception e) {
                    }
                    if (callback != null) {
                        callback.onFailure(errorMsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<SupabaseApi.SignUpResponse> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                if (callback != null) {
                    callback.onFailure("네트워크 오류가 발생했습니다");
                }
            }
        });
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
}
