package views;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


//when RemoteClientViewer.updateMove() send GET request for move, MoveReciever receives it and give sto moveViewController to update text area
@RestController
public class MoveReciever
{
    public static MoveViewController moveViewController;

    //receives move update from client viewer and passes to controller to update view
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/receiveMove/{move}")
    public String receiveMove(@PathVariable String move)
    {
        if (moveViewController != null)
            moveViewController.updateMove(move);
        return "ok";
    }
}