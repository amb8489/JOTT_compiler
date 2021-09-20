



# evERy char we expect to see with its class
lut =    {        " ":0,
                  "#":1,
                  ",":2,
                  "]":3,
                  "[":4,
                  "{":5,
                  "}":6,
                  "=":7,
                  "<":9,
                  ">":9,
                  "/":10,
                  "+":10,
                  "-":10,
                  "*":10,
                  ";":11,
                  ".":12,
                  "diget":13,
                  "letter":15,
                  ":":16,
                  "!":17,
                  "\"":18,
                  "\n":20,
                    }

#ccurrentState = row in
# class = col this will tell us what state to move to next given input classification
#        state next = tm[currentState][class];
F = 0  # finsih
ER = 21 # error state

dfa = [#[0 ,1 ,2 , 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,ER]
        [0 ,1 ,2 , 3, 4, 5, 6, 7,ER, 9,10,11,12,13,ER,15,16 ,17,18,ER,0,ER],     #0  start
        [ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,ER],     #1  # state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #2  , state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #3  ] state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #4  [ state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #5  } state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #6  { state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #7  = state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #8  == <= >= relitiveOp state
        [F ,F ,F ,F ,F ,F ,F ,8 ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #9  < > state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #10  /+-* state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #11  ; state
        [ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,14,ER,ER,ER,ER,ER,ER,ER,ER],     #12  . state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,14,13,F ,F ,F ,F ,F ,F ,F ,ER],     #13  0123456789 state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,14,F ,F ,F ,F ,F ,F ,F ,ER],     #14  isNumbERWithDecimal state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,15,F ,15,F ,F ,F ,F ,F ,ER],     #15  lettER state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #16  : state
        [ER,ER,ER,ER,ER,ER,ER,19,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER],     #17  ! state
        [F,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,20,ER,20,ER,ER,F,ER,ER,ER],     #18  " state
        [F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER],     #19  != state
        [20 ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,20,F,20 ,F ,F ,18 ,F ,F ,ER],     #20  string
        [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],     #ER  ERror
        [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ,0 ,0, 0, 0, 0, 0, 0, 0, 0, 0, 0]      #ER  end or complete token go back to start

      ]



def classify(ch):
    if ch.isdigit():
        return lut["diget"]
    if ch.isalpha():
        return lut["letter"]

    return lut[ch]
    
    
    
    
def tokenize(text):
    next = 0
    token = ""
    prev = 0

    
    for ch in text:
        prev = next

        next = dfa[next][classify(ch)]
        
        
        

        if next == F:
            if token !="":
                print("token:[{}]".format(token))
                token = ""

                if next == F:
                    if ch != " ":
                        token+=ch
                        print("token:[{}]".format(token))
                    token = ""
                


                    
                
        else:
            if next == 1:
                token+=ch
            elif ch != " ":
                token+=ch
         


    print("token:[{}]".format(token))

if __name__ == "__main__":
    
    
    
    
    # text to tokinize
    text =  "ab = 10 b != 7 #comment\n hello,+,=,- aaron,*:;,/ #comment\n \"string\"  8/2 lut[8] = 10"
    
    
    # tokenizing
    tokenize(text)

        
        


        
 
