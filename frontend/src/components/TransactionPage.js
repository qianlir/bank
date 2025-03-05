import React, { useState, useEffect } from 'react';
import { 
  Container,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Grid
} from '@mui/material';
import axios from 'axios';

function TransactionPage() {
  const [transactions, setTransactions] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [currentTransaction, setCurrentTransaction] = useState(null);
  const [formData, setFormData] = useState({
    type: 'INCOME',
    amount: '',
    description: ''
  });

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/transactions');
      setTransactions(response.data);
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  const handleOpenDialog = (transaction = null) => {
    setCurrentTransaction(transaction);
    setFormData(transaction || {
      type: 'INCOME',
      amount: '',
      description: ''
    });
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setCurrentTransaction(null);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (currentTransaction) {
        await axios.put(`http://localhost:8080/api/transactions/${currentTransaction.id}`, formData);
      } else {
        await axios.post('http://localhost:8080/api/transactions', formData);
      }
      fetchTransactions();
      handleCloseDialog();
    } catch (error) {
      console.error('Error saving transaction:', error);
      alert(`保存失败: ${error.response?.data?.message || error.message}`);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/transactions/${id}`);
      fetchTransactions();
    } catch (error) {
      console.error('Error deleting transaction:', error);
    }
  };

  return (
    <Container maxWidth="lg">
      <Typography variant="h4" gutterBottom>
        交易管理
      </Typography>
      
      <Button 
        variant="contained" 
        color="primary"
        onClick={() => handleOpenDialog()}
        style={{ marginBottom: 20 }}
      >
        新建交易
      </Button>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>类型</TableCell>
              <TableCell>金额</TableCell>
              <TableCell>描述</TableCell>
              <TableCell>操作</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {transactions.map((transaction) => (
              <TableRow key={transaction.id}>
                <TableCell>{transaction.type === 'INCOME' ? '收入' : '支出'}</TableCell>
                <TableCell>{transaction.amount}</TableCell>
                <TableCell>{transaction.description}</TableCell>
                <TableCell>
                  <Button 
                    color="primary"
                    onClick={() => handleOpenDialog(transaction)}
                  >
                    编辑
                  </Button>
                  <Button 
                    color="secondary"
                    onClick={() => handleDelete(transaction.id)}
                  >
                    删除
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {currentTransaction ? '编辑交易' : '新建交易'}
        </DialogTitle>
        <DialogContent>
          <form onSubmit={handleSubmit}>
            <Grid container spacing={3} style={{ marginTop: 10 }}>
              <Grid item xs={12}>
                <TextField
                  select
                  fullWidth
                  label="类型"
                  name="type"
                  value={formData.type}
                  onChange={handleChange}
                  SelectProps={{
                    native: true,
                  }}
                  required
                >
                  <option value="INCOME">收入</option>
                  <option value="EXPENSE">支出</option>
                </TextField>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="金额"
                  name="amount"
                  type="number"
                  value={formData.amount}
                  onChange={handleChange}
                  required
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="描述"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  required
                />
              </Grid>
            </Grid>
          </form>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>取消</Button>
          <Button onClick={handleSubmit} color="primary">
            保存
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default TransactionPage;
