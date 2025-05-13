import React, { useState } from 'react';

function Calculator() {
  const [expression, setExpression] = useState('');
  const [result, setResult] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch('http://localhost:8081/api/calculator/evaluate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ expression })
      });

      const data = await res.text(); // on suppose que tu renvoies juste un string (le résultat)
      setResult(data);
    } catch (error) {
      console.error('Erreur : ', error);
      setResult('Erreur serveur');
    }
  };

  return (
    <div>
      <h1>Calculatrice</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={expression}
          onChange={(e) => setExpression(e.target.value)}
          placeholder="Tapez une expression"
        />
        <button type="submit">Calculer</button>
      </form>
      <h2>Résultat : {result}</h2>
    </div>
  );
}

export default Calculator;
