import React from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@mui/material';
import { Link } from 'react-router-dom';

function TransactionList({ transactions }) {
  return (
    <div>
      <Button
        variant="contained"
        color="primary"
        component={Link}
        to="/create"
        style={{ marginBottom: '20px' }}
      >
        创建新交易
      </Button>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>金额</TableCell>
              <TableCell>货币</TableCell>
              <TableCell>描述</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {transactions.map((transaction) => (
              <TableRow key={transaction.id}>
                <TableCell>{transaction.id}</TableCell>
                <TableCell>{transaction.amount}</TableCell>
                <TableCell>{transaction.currency}</TableCell>
                <TableCell>{transaction.description}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default TransactionList;
