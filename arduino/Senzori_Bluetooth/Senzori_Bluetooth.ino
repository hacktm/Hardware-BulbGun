#include <SoftwareSerial.h>
// se conecteaza rx-ul de la bluetooth la tx-ul de la arduino
// se conecteaza tx-ul de la bluetooth la rx-ul de la arduino
//vcc-ul la 3,3V
//gnd la gnd
//poti sa trimiti folosind serial monitorul

int stare1=0;
int photocellPin = 0;
int photocellReading;
int LEDpin = 13;
int LEDbrightness;

int photocellPin2 = 1;
int photocellReading2;
int LEDpin2 = 8;
int LEDbrightness2;
int stare2=0;

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

  pinMode(LEDpin,OUTPUT);
  pinMode(LEDpin2, OUTPUT);
  
  pinMode(RELAY_1, OUTPUT);
  pinMode(RELAY_2, OUTPUT);
  
  digitalWrite(RELAY_1, RELAY_OFF);
  digitalWrite(RELAY_2, RELAY_OFF);
 }

void loop(){
   //Read from bluetooth and write to usb serial 

  Bluetooth();
  Senzori();
  delay(500);
  Serial1.print(String(stare1,DEC)+"\n");
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
  Serial.print("Analog reading = ");
  Serial.println(photocellReading);
  
  photocellReading2 = analogRead(photocellPin2);
  Serial.print("Analog reading2 = ");
  Serial.println(photocellReading2);    
 
  if(photocellReading>SWITCH){
    stare1=1-stare1;
    
    if(stare1==0){
       digitalWrite(LEDpin,LOW);
       digitalWrite(RELAY_1, RELAY_OFF);
    }else if(stare1==1){
      digitalWrite(LEDpin,HIGH);
      digitalWrite(RELAY_1, RELAY_ON);
      }
  }
  
  if(photocellReading2>SWITCH){
    stare2=1-stare2;
    if(stare2==0){
      digitalWrite(LEDpin2,LOW);
      digitalWrite(RELAY_2, RELAY_OFF); 
    }else if(stare2==1){
      digitalWrite(LEDpin2,HIGH);
      digitalWrite(RELAY_2, RELAY_ON);
      }
  }
  
  
}

