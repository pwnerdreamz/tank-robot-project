package control.tank.com.pwner.tankcontrollingapp;

import java.io.IOException;

public interface JoystickMovedListener {
    public void OnMoved(int pan, int tilt) throws IOException;
    public void OnReleased();
    public void OnReturnedToCenter();
}