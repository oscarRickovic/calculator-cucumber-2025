import React, { useState, useEffect, useRef } from 'react';
import './CalculatorDesign.css';

const CalculatorDesign = () => {
  // States
  const [display, setDisplay] = useState('');
  const [expressionHistory, setExpressionHistory] = useState('');
  const [currentInput, setCurrentInput] = useState('');
  const [isScientificMode, setIsScientificMode] = useState(false);
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [calculationHistory, setCalculationHistory] = useState([]);
  const [historyIndex, setHistoryIndex] = useState(-1);
  const [memoryValue, setMemoryValue] = useState(0.0);
  const [hasMemory, setHasMemory] = useState(false);

  // Refs
  const displayRef = useRef(null);

  // Utility functions
  const isLastCharacterOperator = () => {
    if (currentInput.length === 0) return true;
    const lastChar = currentInput.charAt(currentInput.length - 1);
    return "+-*/^%".indexOf(lastChar) >= 0;
  };

  const getLastNumber = () => {
    const text = currentInput;
    let lastOperatorIndex = -1;
    
    for (let i = text.length - 1; i >= 0; i--) {
      const c = text.charAt(i);
      if ("+-*/^%()".indexOf(c) >= 0) {
        lastOperatorIndex = i;
        break;
      }
    }
    
    return lastOperatorIndex < 0 ? text : text.substring(lastOperatorIndex + 1);
  };

  // Evaluation functions
  const evaluateExpression = () => {
    if (currentInput.length === 0) return;
    
    try {
      // Save the expression for history
      const expression = currentInput;
      setExpressionHistory(expression);
      
      // Simple eval for demo (in a real app, use a proper expression parser)
      // This is a placeholder for the actual calculation logic
      const result = Function('"use strict";return (' + expression.replace(/PI/g, 'Math.PI')
                                                         .replace(/E/g, 'Math.E')
                                                         .replace(/PHI/g, '(1 + Math.sqrt(5)) / 2')
                                                         .replace(/sin/g, 'Math.sin')
                                                         .replace(/cos/g, 'Math.cos')
                                                         .replace(/tan/g, 'Math.tan')
                                                         .replace(/asin/g, 'Math.asin')
                                                         .replace(/acos/g, 'Math.acos')
                                                         .replace(/atan/g, 'Math.atan')
                                                         .replace(/log/g, 'Math.log10')
                                                         .replace(/ln/g, 'Math.log')
                                                         .replace(/sqrt/g, 'Math.sqrt')
                                                         .replace(/\^/g, '**') + ')')();
      
      // Update display with the result
      setDisplay(result.toString());
      
      // Save to history
      setCalculationHistory([...calculationHistory, expression]);
      setHistoryIndex(calculationHistory.length);
      
      // Reset the current input to the result for chaining calculations
      setCurrentInput(result.toString());
    } catch (e) {
      setDisplay(`Error: ${e.message}`);
      setCurrentInput('');
    }
  };

  // Handler functions
  const handleButtonClick = (label) => {
    switch (label) {
      case "C":
        clearDisplay();
        break;
      case "=":
        evaluateExpression();
        break;
      case ".":
        addDecimalPoint();
        break;
      case "⌫": // Backspace
        handleDelete();
        break;
      case "÷":
        appendOperator("/");
        break;
      case "×":
        appendOperator("*");
        break;
      case "+":
      case "-":
        appendOperator(label);
        break;
      case "(":
      case ")":
        setCurrentInput(currentInput + label);
        updateDisplay(currentInput + label);
        break;
      // Memory operations
      case "MC":
        setMemoryValue(0.0);
        setHasMemory(false);
        updateDisplay(currentInput);
        break;
      case "MR":
        if (hasMemory) {
          const newInput = currentInput + memoryValue.toString();
          setCurrentInput(newInput);
          updateDisplay(newInput);
        }
        break;
      case "M+":
        if (currentInput.length > 0) {
          try {
            const newMemValue = memoryValue + parseFloat(evaluateCurrentInput());
            setMemoryValue(newMemValue);
            setHasMemory(true);
          } catch (ex) {
            setDisplay("Error");
          }
        }
        break;
      case "M-":
        if (currentInput.length > 0) {
          try {
            const newMemValue = memoryValue - parseFloat(evaluateCurrentInput());
            setMemoryValue(newMemValue);
            setHasMemory(true);
          } catch (ex) {
            setDisplay("Error");
          }
        }
        break;
      // Scientific operations
      case "sin":
      case "cos":
      case "tan":
      case "asin":
      case "acos":
      case "atan":
      case "log":
      case "ln":
        applyFunction(label);
        break;
      case "π":
        setCurrentInput(currentInput + "PI");
        updateDisplay(currentInput + "PI");
        break;
      case "e":
        setCurrentInput(currentInput + "E");
        updateDisplay(currentInput + "E");
        break;
      case "i": // Imaginary unit
        setCurrentInput(currentInput + "i");
        updateDisplay(currentInput + "i");
        break;
      case "φ": // Golden ratio
        setCurrentInput(currentInput + "PHI");
        updateDisplay(currentInput + "PHI");
        break;
      case "x²":
        applyPower(2);
        break;
      case "x³":
        applyPower(3);
        break;
      case "√x":
        applyFunction("sqrt");
        break;
      case "∛x":
        if (currentInput.length > 0) {
          const expr = currentInput;
          const newExpr = `(${expr})^(1.0/3)`;
          setCurrentInput(newExpr);
          updateDisplay(newExpr);
        }
        break;
      case "xʸ":
        if (currentInput.length > 0) {
          setCurrentInput(currentInput + "^");
          updateDisplay(currentInput + "^");
        }
        break;
      case "1/x":
        if (currentInput.length > 0) {
          const expr = currentInput;
          const newExpr = `(1.0/${expr})`;
          setCurrentInput(newExpr);
          updateDisplay(newExpr);
        }
        break;
      case "%":
        appendOperator("%");
        break;
      case "!":
        if (currentInput.length > 0) {
          setCurrentInput(currentInput + "!");
          updateDisplay(currentInput + "!");
        }
        break;
      default:
        // Handle numbers and other input
        setCurrentInput(currentInput + label);
        updateDisplay(currentInput + label);
    }
  };

  const handleKeyPress = (event) => {
    const key = event.key;
    const code = event.code;
    
    if (/^\d$/.test(key)) {
      handleButtonClick(key);
      event.preventDefault();
    } else if (key === "." || key === ",") {
      handleButtonClick(".");
      event.preventDefault();
    } else if (key === "=" || key === "Enter") {
      handleButtonClick("=");
      event.preventDefault();
    } else if (key === "+") {
      handleButtonClick("+");
      event.preventDefault();
    } else if (key === "-") {
      handleButtonClick("-");
      event.preventDefault();
    } else if (key === "*") {
      handleButtonClick("×");
      event.preventDefault();
    } else if (key === "/") {
      handleButtonClick("÷");
      event.preventDefault();
    } else if (key === "Backspace") {
      handleButtonClick("⌫");
      event.preventDefault();
    } else if (key === "Escape") {
      handleButtonClick("C");
      event.preventDefault();
    } else if (key === "(" || key === "[") {
      handleButtonClick("(");
      event.preventDefault();
    } else if (key === ")" || key === "]") {
      handleButtonClick(")");
      event.preventDefault();
    } else if (key === "ArrowUp") {
      navigateHistory(-1);
      event.preventDefault();
    } else if (key === "ArrowDown") {
      navigateHistory(1);
      event.preventDefault();
    }
  };

  useEffect(() => {
    window.addEventListener('keydown', handleKeyPress);
    return () => {
      window.removeEventListener('keydown', handleKeyPress);
    };
  }, [currentInput, calculationHistory, historyIndex]);

  const evaluateCurrentInput = () => {
    try {
      // Simple eval for demo (in a real app, use a proper expression parser)
      return Function('"use strict";return (' + currentInput.replace(/PI/g, 'Math.PI')
                                                          .replace(/E/g, 'Math.E')
                                                          .replace(/PHI/g, '(1 + Math.sqrt(5)) / 2')
                                                          .replace(/sin/g, 'Math.sin')
                                                          .replace(/cos/g, 'Math.cos')
                                                          .replace(/tan/g, 'Math.tan')
                                                          .replace(/asin/g, 'Math.asin')
                                                          .replace(/acos/g, 'Math.acos')
                                                          .replace(/atan/g, 'Math.atan')
                                                          .replace(/log/g, 'Math.log10')
                                                          .replace(/ln/g, 'Math.log')
                                                          .replace(/sqrt/g, 'Math.sqrt')
                                                          .replace(/\^/g, '**') + ')')().toString();
    } catch (e) {
      return "Error";
    }
  };

  const navigateHistory = (direction) => {
    if (calculationHistory.length === 0) return;
    
    const newIndex = historyIndex + direction;
    
    if (newIndex < 0 || newIndex >= calculationHistory.length) return;
    
    setHistoryIndex(newIndex);
    setCurrentInput(calculationHistory[newIndex]);
    updateDisplay(calculationHistory[newIndex]);
  };

  const applyFunction = (functionName) => {
    if (currentInput.length > 0) {
      // Apply function to the current expression
      const expr = currentInput;
      const newExpr = `${functionName}(${expr})`;
      setCurrentInput(newExpr);
      updateDisplay(newExpr);
    } else {
      // If no input, just add the function name with open parenthesis
      setCurrentInput(`${functionName}(`);
      updateDisplay(`${functionName}(`);
    }
  };

  const applyPower = (power) => {
    if (currentInput.length > 0) {
      const expr = currentInput;
      const newExpr = `(${expr})^${power}`;
      setCurrentInput(newExpr);
      updateDisplay(newExpr);
    }
  };

  const appendOperator = (operator) => {
    if (currentInput.length > 0) {
      const lastChar = currentInput.charAt(currentInput.length - 1);
      
      // Don't append operator if last character is already an operator
      if ("+-*/^%".indexOf(lastChar) >= 0) {
        // Replace the last operator with the new one
        const newInput = currentInput.slice(0, -1) + operator;
        setCurrentInput(newInput);
        updateDisplay(newInput);
      } else {
        setCurrentInput(currentInput + operator);
        updateDisplay(currentInput + operator);
      }
    } else if (operator === "-") {
      // Allow negative numbers
      setCurrentInput("-");
      updateDisplay("-");
    }
  };

  const addDecimalPoint = () => {
    if (currentInput.length === 0 || isLastCharacterOperator()) {
      setCurrentInput(currentInput + "0.");  // Ensure proper decimal handling
      updateDisplay(currentInput + "0.");
    } else if (!getLastNumber().includes(".")) {
      setCurrentInput(currentInput + ".");
      updateDisplay(currentInput + ".");
    }
  };

  const clearDisplay = () => {
    setCurrentInput('');
    setDisplay('');
    setExpressionHistory('');
  };

  const handleDelete = () => {
    if (currentInput.length > 0) {
      const newInput = currentInput.slice(0, -1);
      setCurrentInput(newInput);
      updateDisplay(newInput);
    }
  };

  const updateDisplay = (input) => {
    setDisplay(input);
    
    // Check for unbalanced parentheses
    let openCount = 0;
    let closeCount = 0;
    
    for (let i = 0; i < input.length; i++) {
      if (input.charAt(i) === '(') openCount++;
      if (input.charAt(i) === ')') closeCount++;
    }
    
    if (displayRef.current) {
      if (openCount !== closeCount) {
        displayRef.current.classList.add('error-border');
      } else {
        displayRef.current.classList.remove('error-border');
      }
    }
  };

  // Button rendering functions
  const renderBasicButtonGrid = () => {
    const buttonData = [
      [["7", "number"], ["8", "number"], ["9", "number"], ["÷", "operation"]],
      [["4", "number"], ["5", "number"], ["6", "number"], ["×", "operation"]],
      [["1", "number"], ["2", "number"], ["3", "number"], ["-", "operation"]],
      [["0", "number"], [".", "number"], ["C", "operation"], ["+", "operation"]],
      [["=", "operation"], ["⌫", "operation"], ["(", "scientific"], [")", "scientific"]]
    ];

    return (
      <div className={`button-grid basic-grid ${isDarkMode ? 'dark' : 'light'}`}>
        {buttonData.map((row, rowIndex) => (
          <div className="button-row" key={`basic-row-${rowIndex}`}>
            {row.map((item, colIndex) => {
              const [label, type] = item;
              return (
                <button 
                  key={`basic-${rowIndex}-${colIndex}`} 
                  className={`calc-button ${type} ${isDarkMode ? 'dark' : 'light'}`}
                  onClick={() => handleButtonClick(label)}
                >
                  {label}
                </button>
              );
            })}
          </div>
        ))}
      </div>
    );
  };

  const renderMemoryButtonGrid = () => {
    const memoryButtons = [
      ["MC", "memory"], ["MR", "memory"], ["M+", "memory"], ["M-", "memory"]
    ];

    return (
      <div className={`button-grid memory-grid ${isDarkMode ? 'dark' : 'light'}`}>
        <div className="button-row">
          {memoryButtons.map(([label, type], index) => (
            <button 
              key={`memory-${index}`} 
              className={`calc-button ${type} ${isDarkMode ? 'dark' : 'light'}`}
              onClick={() => handleButtonClick(label)}
            >
              {label}
            </button>
          ))}
        </div>
      </div>
    );
  };

  const renderScientificButtonGrid = () => {
    const scientificData = [
      [["sin", "scientific"], ["cos", "scientific"], ["tan", "scientific"], ["ln", "scientific"]],
      [["asin", "scientific"], ["acos", "scientific"], ["atan", "scientific"], ["log", "scientific"]],
      [["π", "scientific"], ["e", "scientific"], ["i", "scientific"], ["φ", "scientific"]],
      [["x²", "scientific"], ["x³", "scientific"], ["√x", "scientific"], ["∛x", "scientific"]],
      [["xʸ", "scientific"], ["1/x", "scientific"], ["%", "scientific"], ["!", "scientific"]]
    ];

    if (!isScientificMode) return null;

    return (
      <div className={`button-grid scientific-grid ${isDarkMode ? 'dark' : 'light'}`}>
        {scientificData.map((row, rowIndex) => (
          <div className="button-row" key={`scientific-row-${rowIndex}`}>
            {row.map(([label, type], colIndex) => (
              <button 
                key={`scientific-${rowIndex}-${colIndex}`} 
                className={`calc-button ${type} ${isDarkMode ? 'dark' : 'light'}`}
                onClick={() => handleButtonClick(label)}
              >
                {label}
              </button>
            ))}
          </div>
        ))}
      </div>
    );
  };

  return (
    <div className={`calculator-container ${isDarkMode ? 'dark' : 'light'}`}>
      <div className="calculator-inner">
        <div className="display-container">
          <div className={`expression-history ${isDarkMode ? 'dark' : 'light'}`}>
            {expressionHistory}
          </div>
          <input
            ref={displayRef}
            type="text"
            className={`display ${isDarkMode ? 'dark' : 'light'}`}
            value={display}
            readOnly
          />
        </div>
        
        <div className="toggle-container">
          <button 
            className={`toggle-button ${isScientificMode ? 'active' : ''} ${isDarkMode ? 'dark' : 'light'}`}
            onClick={() => setIsScientificMode(!isScientificMode)}
          >
            Scientific
          </button>
          <button 
            className={`toggle-button ${isDarkMode ? 'active dark' : 'light'}`}
            onClick={() => setIsDarkMode(!isDarkMode)}
          >
            Dark Mode
          </button>
        </div>
        
        <div className="button-container">
          {renderMemoryButtonGrid()}
          {renderScientificButtonGrid()}
          {renderBasicButtonGrid()}
        </div>
      </div>
    </div>
  );
};

export default CalculatorDesign;