import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import './index.css';
import App from './App';

import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';

ReactDOM.render(
  <BrowserRouter basename={process.env.REACT_APP_ROUTER_BASENAME}>
    <App />
  </BrowserRouter>,
  document.getElementById('root')
);
