int stare=0;
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

void setup(void){
  
  Serial.begin(9600);
  pinMode(LEDpin,OUTPUT);
  pinMode(LEDpin2, OUTPUT);
  
  pinMode(RELAY_1, OUTPUT);
  pinMode(RELAY_2, OUTPUT);
  
  digitalWrite(RELAY_1, RELAY_OFF);
  digitalWrite(RELAY_2, RELAY_OFF);
 }

void loop(void){
  Senzori();
  delay(500);
}



void Senzori(){
  photocellReading = analogRead(photocellPin);
  Serial.print("Analog reading = ");
  Serial.println(photocellReading);
  photocellReading2 = analogRead(photocellPin2);
  Serial.print("Analog reading2 = ");
  Serial.println(photocellReading2);    
 
  if(photocellReading>SWITCH){
    stare++;
    int x=stare%2;
    if(x==0){
       digitalWrite(LEDpin,LOW);
       digitalWrite(RELAY_1, RELAY_OFF);
    }else if(x==1){
      digitalWrite(LEDpin,HIGH);
      digitalWrite(RELAY_1, RELAY_ON);
      }
  }
  
  if(photocellReading2>SWITCH){
    stare2++;
    int y=stare2%2;
    if(y==0){
      digitalWrite(LEDpin2,LOW);
      digitalWrite(RELAY_2, RELAY_OFF); 
    }else if(y==1){
      digitalWrite(LEDpin2,HIGH);
      digitalWrite(RELAY_2, RELAY_ON);
      }
  }
  
}
