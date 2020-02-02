import React from 'react';
import CalculateCredit from 'pages/CalculateCredit';
import { NotificationContainer } from 'react-notifications';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-notifications/lib/notifications.css';
import './App.css';

function App() {
  return (
    <div className="App">
      <CalculateCredit/>
      <NotificationContainer/>
    </div>
  );
}

export default App;
