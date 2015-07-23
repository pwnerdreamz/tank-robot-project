/*
*   Tank Robot Code 
*   Written by Grigoris Mallios
*/
#include <SoftwareSerial.h>        // Include SoftwareSerial Library
const int motor_left[] = { 2, 3 }; // motor pins
const int motor_right[] ={ 4, 5 }; // motor pins
const int ledpin=13;               // led on D13 will show status
int BluetoothData;                 // data from bluetooth
SoftwareSerial Bluetooth(10, 11);  // RX, TX pins

void setup() {
    Bluetooth.begin(9600);         // Initialise bluetooth port
    Bluetooth.setTimeout(50);      // Set timeout for bluetooth port(makes it go faster for some reason)
    Serial.begin(9600);            // Initialise Serial port for debugging
    pinMode(ledpin, OUTPUT);       // Set led to output
    int i;                         // Loop to init all motor pins
    for(i = 0; i < 2; i++){
        pinMode(motor_left[i], OUTPUT);
        pinMode(motor_right[i], OUTPUT);
    }
    blink();                        // Blink led_pin
}

/*
 *  Blink Function
 */
void blink(){
    digitalWrite(ledpin, 1);
    delay(50);
    digitalWrite(ledpin, 0);
    delay(50);
    digitalWrite(ledpin, 1);
    delay(50);
    digitalWrite(ledpin, 0);
    delay(50);
    digitalWrite(ledpin, 1);
    digitalWrite(ledpin, 0);
    delay(50);
    digitalWrite(ledpin, 1);
    digitalWrite(ledpin, 0);
    delay(50);
    digitalWrite(ledpin, 1);
}

/*
 * Serial Data handling
 */

void loop() {
    if (Bluetooth.available()){           // Check if bluetooth is available
        BluetoothData=Bluetooth.read();   // If available,read the serial port
        if(BluetoothData == 'S'){         // Proccess the received data
            Stop();
            } else if(BluetoothData == 'B'){
            Back();
            } else if(BluetoothData == 'L'){
            Left();
            } else if(BluetoothData == 'R'){
            Right();
            } else if(BluetoothData == 'F'){
            Forward();
            }
            if(Serial.available()){       // Send back to Serial for debugging
              Serial.println("Received: " + BluetoothData);
            }
    }
}


void Back(){
    digitalWrite(motor_left[0], 1);
    digitalWrite(motor_left[1], 0);
    
    digitalWrite(motor_right[0], 0);
    digitalWrite(motor_right[1], 1);
    
}

void Forward(){
    digitalWrite(motor_left[0], 0);
    digitalWrite(motor_left[1], 1);
    
    digitalWrite(motor_right[0], 1);
    digitalWrite(motor_right[1], 0);
}

void Left(){
    digitalWrite(motor_left[0], 1);
    digitalWrite(motor_left[1], 0);
    
    digitalWrite(motor_right[0], 1);
    digitalWrite(motor_right[1], 0);
}

void Stop(){
    digitalWrite(motor_left[0], 0);
    digitalWrite(motor_left[1], 0);
    
    digitalWrite(motor_right[0], 0);
    digitalWrite(motor_right[1], 0);
}

void Right(){
    digitalWrite(motor_left[0], 0);
    digitalWrite(motor_left[1], 1);
    
    digitalWrite(motor_right[0], 0);
    digitalWrite(motor_right[1], 1);
}
