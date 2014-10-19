#include <SoftwareSerial.h>
// se conecteaza rx-ul de la bluetooth la tx-ul de la arduino
// se conecteaza tx-ul de la bluetooth la rx-ul de la arduino
//vcc-ul la 3,3V
//gnd la gnd
//poti sa trimiti folosind serial monitorul

int stare1=0;
int photocellPin = 0;
int photocellReading;

int stare2=0;
int photocellPin2 = 1;
int photocellReading2;

#define trigPin 12
#define echoPin 11

#define SWITCH 500

#define RELAY_1  2
#define RELAY_2  3

#define RELAY_ON 1
#define RELAY_OFF 0


void setup()
{
  //se initializeaza conexiunea seriala si cea prin bluetooth
  Serial.begin(9600);                 //serial cable
  Serial1.begin(9600, SERIAL_8N1);    //serial bluetooth

  
  pinMode(RELAY_1, OUTPUT);
  pinMode(RELAY_2, OUTPUT);
  
  digitalWrite(RELAY_1, RELAY_OFF);
  digitalWrite(RELAY_2, RELAY_OFF);
  
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
 }

void loop(){
  Distance();
  Bluetooth();
  Senzori();
  delay(500);
  Serial1.print(String(stare1,DEC)+"\n");
}

void Distance(){
  long duration, distance;
  digitalWrite(trigPin, LOW);  // Added this line
  delayMicroseconds(2); // Added this line
  digitalWrite(trigPin, HIGH);
  //  delayMicroseconds(1000); - Removed this line
  delayMicroseconds(10); // Added this line
  digitalWrite(trigPin, LOW);
  
  duration = pulseIn(echoPin, HIGH);
  distance = (duration/2) / 29.1;
  
  Serial.print(distance);
  Serial.println("cm");
    
  if (distance >= 5 && distance <= 30){
    stare1=1-stare1;
    if(stare1==0){      
       digitalWrite(RELAY_1, RELAY_OFF);
    }else if(stare1==1){
       digitalWrite(RELAY_1, RELAY_ON);
    }
  }

}


void Bluetooth(){
  if(Serial1.available())
  {
    char toSend = (char)Serial1.read();
    if(toSend=='1'){
      stare1=1-stare1;
      digitalWrite(RELAY_1, RELAY_ON);
      
    }
    if(toSend=='0'){
      stare1=1-stare1;
      digitalWrite(RELAY_1, RELAY_OFF);
    }

  }

}

void Senzori(){
  
 
  photocellReading = analogRead(photocellPin);
//  Serial.print("Analog reading = ");
//  Serial.println(photocellReading);
  
  photocellReading2 = analogRead(photocellPin2);
//  Serial.print("Analog reading2 = ");
//  Serial.println(photocellReading2);    
 
  if(photocellReading>SWITCH){
    stare1=1-stare1;
    
    if(stare1==0){      
       digitalWrite(RELAY_1, RELAY_OFF);
    }else if(stare1==1){
       digitalWrite(RELAY_1, RELAY_ON);
    }
  }
  
  if(photocellReading2>SWITCH){
    stare2=1-stare2;
    if(stare2==0){
      digitalWrite(RELAY_2, RELAY_OFF); 
    }else if(stare2==1){
      digitalWrite(RELAY_2, RELAY_ON);
    }
  }
  
  
}

