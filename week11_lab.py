
import sys
import class_week11

inputs = input("parameters : ").split()
#print(inputs)

#lab6_copy
#parameter : cp file1 file2
if inputs[0] == "cp":
    file1_path = inputs[1]+".txt"
    file2_path = inputs[2]+".txt"
    print(file1_path,file2_path)

    #read
    f = open(file1_path,'r')
    data = f.read()
    f.close()

    #write
    f2 = open(file2_path,'w')
    f2.write(data)
    f2.close()

#lab7_countLine,Word
elif inputs[0] == "wc":
    file1_path = inputs[1]+".txt"

    f = open(file1_path,'r')
    lines = f.readlines()
    f.close()

    cLine = 0
    cWord = 0
    for line in lines:
        cLine+=1
        cWord+=len(line.split())

    print("# of lines : ",cLine)
    print("# of words : ",cWord)

#lab8_calculator
elif inputs[0] == "calc":
    cal = class_week11.Calc()
    cal.sum(5,2)
    cal.subtraction(5,2)
    cal.multiplication(5,2)
    cal.division(5,2)

#lab9_stack
elif inputs[0] == "stack":
    formula = input("enter formula : ").split()
    #print(formula)
    stck = class_week11.StackCalc()

    formula_ = class_week11.StackCalc().toPostfix(formula)
    print("result : ",stck.calc(formula_))