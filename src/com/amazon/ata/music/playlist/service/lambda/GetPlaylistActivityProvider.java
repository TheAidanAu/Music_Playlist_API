package com.amazon.ata.music.playlist.service.lambda;

//import com.amazon.ata.music.playlist.service.dependency.App;
import com.amazon.ata.music.playlist.service.dependency.DaggerServiceComponent;
import com.amazon.ata.music.playlist.service.dependency.ServiceComponent;
import com.amazon.ata.music.playlist.service.models.requests.GetPlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.GetPlaylistResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPlaylistActivityProvider implements RequestHandler<GetPlaylistRequest, GetPlaylistResult> {

    private static ServiceComponent serviceComponent;

    public GetPlaylistActivityProvider() {

    }

    @Override
    public GetPlaylistResult handleRequest(final GetPlaylistRequest getPlaylistRequest, Context context) {
        //when we want to use the GetPlaylistActivity handler,
        // we first go through the GetPlaylistActivityProvider class
        // which instantiates a GetPlaylistActivity (a root object)
        // along with a playlistDAO (dependency) and DynamoDBMapper (dependency)
        return getServiceComponent().provideGetPlaylistActivity().handleRequest(getPlaylistRequest, context);

    }

    private ServiceComponent getServiceComponent() {
        // Singleton Pattern
        // return the generated DaggerServiceComponent instead
        if (serviceComponent == null) {
            serviceComponent = DaggerServiceComponent.create();
        }

        return serviceComponent;
    }
}
