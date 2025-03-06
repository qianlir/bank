import React from 'react';
import { Container, Typography } from '@mui/material';
import TransactionManagement from './components/TransactionManagement';

function App() {
  return (
    <Container maxWidth="lg">
      <Typography variant="h3" component="h1" gutterBottom>
        交易系统
      </Typography>
      <TransactionManagement />
    </Container>
  );
}

export default App;
