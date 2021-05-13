class Calc:
    def sum(self,a,b):
        result = a+b
        print("{0} + {1} = {2} 입니다.".format(a,b,result))
    def subtraction(self,a,b):
        result = a-b
        print("{0} - {1} = {2} 입니다.".format(a,b,result))
    def multiplication(self, a, b):
        result = a * b
        print("{0} * {1} = {2} 입니다.".format(a, b, result))
    def division(self, a, b):
        result = a / b
        print("{0} / {1} = {2} 입니다.".format(a, b, result))

from collections import deque
class StackCalc:
    def toPostfix(self,formula):
        strings = ''.join(formula)
        res = []
        stack = []
        for x in strings:
            if x.isdecimal():
                res.append(x)
            else:
                if x == '(':
                    stack.append(x)
                elif x == '*' or x == '/':
                    while stack and (stack[-1] == '*' or stack[-1] == '/'):
                        res.append(stack.pop())
                    stack.append(x)
                elif x == '+' or x == '-':
                    while stack and stack[-1] != '(':
                        res.append(stack.pop())
                    stack.append(x)
                elif x == ')':
                    while stack and stack[-1] != '(':
                        res.append(stack.pop())
                    stack.pop()
        while stack:
            res.append(stack.pop())
        #print(res)
        return res

    def calc(self,formula):
        operators = ['+', '-', '*', '/']
        s = deque()
        for f in formula:
            if f not in operators:
                s.append(int(f))
            elif f == '+':
                n1 = s.pop()
                n2 = s.pop()
                s.append(n2+n1)
            elif f == '-':
                n1 = s.pop()
                n2 = s.pop()
                s.append(n2 - n1)
            elif f == '*':
                n1 = s.pop()
                n2 = s.pop()
                s.append(n2 * n1)
            elif f == '/':
                n1 = s.pop()
                n2 = s.pop()
                s.append(n2 / n1)
        return s.pop()
