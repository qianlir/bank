import React from 'react';
import { Container, Typography } from '@mui/material';
import TransactionPage from './components/TransactionPage';

function App() {
  return (
    <Container maxWidth="lg">
      <Typography variant="h3" component="h1" gutterBottom>
        交易系统
      </Typography>
      <TransactionPage />
    </Container>
  );
}

export default App;
