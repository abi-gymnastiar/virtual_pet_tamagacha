package Physics.interfaces;

import org.joml.Vector2f;

public interface Circle {
    float getRadius();
    Vector2f getPosition();
    Vector2f getVelocity();
    Vector2f getAcceleration();
    void update(float dt, float screenWidth, float screenHeight);
    void applyForce(Vector2f force);
    void applyTorque(float torque);
    void updateAngularVelocity(float dt);
    float getAngularVelocity();
    void setTorque (float torque);
    float getTorque ();
}
