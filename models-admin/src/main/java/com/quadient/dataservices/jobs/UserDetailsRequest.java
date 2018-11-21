package com.quadient.dataservices.jobs;

import com.quadient.dataservices.users.model.UserInformationResponse;

public class UserDetailsRequest extends SimpleGetRequest<UserInformationResponse> {

    private final String userId;

    public UserDetailsRequest(String userId) {
        this.userId = userId;
    }

    @Override
    public String getPath() {
        return "/users/v1/" + userId;
    }

    @Override
    public Class<UserInformationResponse> getResponseBodyClass() {
        return UserInformationResponse.class;
    }

}
