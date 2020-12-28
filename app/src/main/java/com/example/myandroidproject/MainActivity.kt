package com.example.myandroidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {
    private val currentInputNumSB=StringBuilder()
    private val numsList= mutableListOf<String>()
    private val operatorsList= mutableListOf<String>()
    //%相关
    private var percentflag=true
    //判断是否为乘除后的正负
    private var ope=""
    private var plusOrMinus=true
    //判断输入的第一个字符是数字还是运算符号
    private var flagzero=true
    //数字开始的标识符
    private var isNumStart=true

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //清空按钮
        ac.setOnClickListener{
            clearButtonClicked(it)
        }
        //返回按钮
        imageView.setOnClickListener{
            backButtonClicked(it)
        }
        //运算方法
        divide.setOnClickListener {
            operatorButtonClicked(it)
        }
        multiply.setOnClickListener {
            operatorButtonClicked(it)
        }
        addition.setOnClickListener {
            operatorButtonClicked(it)
        }
        subtraction.setOnClickListener {
            operatorButtonClicked(it)
        }
        //数字
        num0.setOnClickListener(this)
        num1.setOnClickListener(this)
        num2.setOnClickListener(this)
        num3.setOnClickListener(this)
        num4.setOnClickListener(this)
        num5.setOnClickListener(this)
        num6.setOnClickListener(this)
        num7.setOnClickListener(this)
        num8.setOnClickListener(this)
        num9.setOnClickListener(this)
        decimal.setOnClickListener(this)
//        %键
        percent.setOnClickListener {
            percentButtonClick(it)
        }
        //等于符号
        equalSymbol.setOnClickListener {
            equalButtonClicked(it)
        }
    }

    override fun onClick(v: View?) {
        //数字调用这个方法
        numberButtonClick(v!!)
    }
    //%键
    fun percentButtonClick(view: View){
        if(numsList.size>operatorsList.size){
            var num=numsList[numsList.size-1].toDouble()
            var numre=realCalculate(num,"x",0.01)
            numsList[numsList.size-1]=numre.toString()
            percentflag=false
        }
        showUI()
        calculate()

    }
    //数字键
    fun numberButtonClick(view: View){
        //将view强制转换为TextView
        val tv=view as TextView
        //对小数点的限制
        var flag=true
        if(tv.text.toString()=="." &&
            (numsList.size==0 || numsList.size==operatorsList.size)){
                flag=false
        }
        if(flag&&percentflag){
            currentInputNumSB.append(tv.text)
            if(isNumStart){
                //当前输入的是一个新的数字，添加到数组中
                //判断是否为正负
                if(plusOrMinus==false){
                    var num=tv.text.toString().toDouble();
                    //算出这个位置应该存的值
                    var result=realCalculate(0.0,ope,num)
                    numsList.add(result.toString())
                    plusOrMinus==true
                    isNumStart=false
                } else{
                    numsList.add(tv.text.toString())
                    //更改isNumStart状态
                    isNumStart=false
                }
            }else{
                if(numsList[numsList.size-1]=="0" && tv.text.toString()!="."){
                    numsList[numsList.size-1]=tv.text.toString()
                }else{
                    //用当前的数字去替换数组中最后一个元素
                    numsList[numsList.size-1]=currentInputNumSB.toString()
                }
        }
        //显示内容
        showUI()
        //计算结果
        calculate()
        }
    }

    //运算符键
    fun operatorButtonClicked(view: View){
        //判断百分号
        percentflag=true
        //判断正负
        plusOrMinus=true
        //将view强制转换为TextView
        val tv=view as TextView
        //输入第一个即为数字
        if(numsList.size>0){
            //一个数字一个运算符
            if(operatorsList.size<numsList.size){
                //保存当前运算符
                operatorsList.add(tv.text.toString())
                //改变状态
                isNumStart=true
                currentInputNumSB.clear()
                //显示内容
                showUI()
            }else{
                //运算符较多，即*-类似
                val si=operatorsList.size-1
                if((operatorsList[si]=="÷"||operatorsList[si]=="x")&&(tv.text.toString()=="+"||tv.text.toString()=="—")){
                    ope=tv.text.toString()
                    plusOrMinus=false
                }
                if((operatorsList[si]=="+"||operatorsList[si]=="—")&&(tv.text.toString()=="+"||tv.text.toString()=="—")){
                    operatorsList[si]=tv.text.toString()
                }
            }
        }else{
          if(tv.text.toString()=="—"||tv.text.toString()=="+"){
              //输入的第一个为运算符，自动添加numsList第一个值为0
              numsList.add("0")
              //flagzero为下方显示所用
              flagzero=false
              //保存当前运算符
              operatorsList.add(tv.text.toString())
              //改变状态
              isNumStart=true
              currentInputNumSB.clear()
          }
        }
        //显示内容
        showUI()
    }

    //清空键--输入框和保存的StringBuilder
    fun clearButtonClicked(view: View){
        process_textview.text=""
        result_textview.text="0"
        currentInputNumSB.clear()
        operatorsList.clear()
        numsList.clear()
        isNumStart=true
        plusOrMinus=true
        flagzero=true

    }

    //撤销键
    @ExperimentalStdlibApi
    fun backButtonClicked(view: View){
        //判断应该撤销运算符还是数字
        if(numsList.size>operatorsList.size){
            if(numsList.size>0){
                //撤销数字
                numsList.removeLast()
                isNumStart=true
                currentInputNumSB.clear()
            }
        }else{
            if(operatorsList.size>0){
                //撤销运算符
                operatorsList.removeLast()
                isNumStart=false
                if(numsList.size>0){
                    //将最后一个元素追加到StringBuilder中
                    currentInputNumSB.append(numsList.last())
                }
            }
        }
        showUI()
        calculate()
    }

    //等于键
    fun equalButtonClicked(view: View){
        if(result_textview.text!=""){
            var str=result_textview.text.toString()
            process_textview.text=str
            result_textview.text=""
            currentInputNumSB.clear()
            operatorsList.clear()
            numsList.clear()
            numsList.add(str)
            isNumStart=true
        }
    }

    //拼接当前运算的表达式，显示到界面上
    private fun showUI(){
        //页面显示内容--拼接后的总内容
        val str=StringBuilder()
        for((i,num) in numsList.withIndex()){
            //判断是否为0
            if(numsList[0]=="0" && i==0){
                flagzero=false
            }else{
                flagzero=true
            }
            //将当前的数字拼接
            if(flagzero==true){
                str.append(num)
            }
            //判断运算符数组中对应位置是否有内容
            if(operatorsList.size>i){
                //将i对应的运算符拼接到字符串中
                str.append(" ${operatorsList[i]} ")
            }
        }
        //显示到页面中
        process_textview.text=str.toString()
    }

    //实现逻辑运算功能
    private fun calculate(){
        //有数字才进行这些工作
        if(numsList.size>0){
            //i记录运算符数组遍历时的下标
            var i=0
            //记录第一个运算符--数字数组的第一个数
            var param1=numsList[0].toDouble()
            var param2=0.0
            if(operatorsList.size>0){
                while (true){
                    //获取i对应的运算符
                    var operator=operatorsList[i]

                    //判断是不是乘除
                    if(operator=="x"||operator=="÷"){
                        //乘除直接运算--找到第二个运算数
                        if(i+1<numsList.size){
                            param2=numsList[i+1].toDouble()
                            //运算
                            param1=realCalculate(param1,operator,param2)
                        }
                    }else{
                        //判断是不是最后一个或者后面不是乘除--直接运算
                        if(i==operatorsList.size-1 ||
                            (operatorsList[i+1]!="x" && operatorsList[i+1]!="÷")){
                            if(i<numsList.size-1){
                                param2=numsList[i+1].toDouble()
                                param1=realCalculate(param1,operator,param2)
                            }
                        }else{
                            //后面有而且是乘除
                            var j=i+1
                            var mparm1=numsList[j].toDouble()
                            var mparm2=0.0
                            while(true){
                                //获取j对应的运算符
                                if(operatorsList[j]=="x"||operatorsList[j]=="÷"){
                                    if(j<numsList.size-1){
                                        mparm2=numsList[j+1].toDouble()
                                        mparm1=realCalculate(mparm1,operatorsList[j],mparm2)
                                    }
                                }else{
                                    //之前运算符后所有连续乘除都运算了--其结果为之前运算符的第二个运算数
                                    break
                                }
                                j++
                                //已经运算到最后了
                                if(j==operatorsList.size){
                                    break
                                }
                            }
                            param2=mparm1
                            param1=realCalculate(param1,operator,param2)
                            //索引值代表运算到那一位，-1防止遗漏一位
                            i=j-1
                        }
                    }
                    i++
                    if(i==operatorsList.size){
                        //遍历结束
                        break
                    }
                }
            }
            //显示对应的结果
            //result_textview.text= String.format("%.4f",param1)
            result_textview.text=param1.toFloat().toString()
        }else{
            result_textview.text="0"
        }
    }

    //运算
    private fun realCalculate(param1:Double,operator: String,param2:Double):Double{
        var result:Double=0.0
        when(operator){
            "+"->{
                result=param1+param2
            }
            "—"->{
                result=param1-param2
            }
            "x"->{
                result=param1*param2
            }
            "÷"->{
                result=param1/param2
            }
        }
        return result
    }
}