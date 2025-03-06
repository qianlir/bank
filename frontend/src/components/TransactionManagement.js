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
  TextField,
  Button,
  Grid,
  Select,
  MenuItem,
  Pagination,
  Box
} from '@mui/material';
import axios from 'axios';

function TransactionManagement() {
  const [accounts, setAccounts] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [totalPages, setTotalPages] = useState(1);
  const [page, setPage] = useState(1);
  const [rowsPerPage] = useState(10);
  const [transactionForm, setTransactionForm] = useState({
    fromAccountNumber: '',
    toAccountNumber: '',
    type: 'TRANSFER',
    amount: '',
    description: ''
  });

  // 获取账户数据
  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/accounts');
        if (response.data && Array.isArray(response.data)) {
          setAccounts(response.data);
        } else {
          console.error('获取的账户数据格式不正确');
          setAccounts([]);
        }
      } catch (error) {
        console.error('获取账户数据失败:', error);
        setAccounts([]);
      }
    };
    fetchAccounts();
  }, []);

  // 获取交易数据
  useEffect(() => {
    const fetchTransactions = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await axios.get('http://localhost:8080/api/transactions', {
          params: {
            page: page - 1,
            size: rowsPerPage
          }
        });
        console.log(response.data);
        setTransactions(response.data || []);
        setTotalPages(response.data.totalPages || 1);
      } catch (error) {
        console.error('获取交易数据失败:', error);
        setError(error);
        setTransactions([]);
      } finally {
        setIsLoading(false);
      }
    };
    fetchTransactions();
  }, [page, rowsPerPage]);

  // 处理表单变化
  const handleFormChange = (e) => {
    setTransactionForm({
      ...transactionForm,
      [e.target.name]: e.target.value
    });
  };

  // 提交交易
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/transactions', {
        type: transactionForm.type,
        amount: parseFloat(transactionForm.amount),
        description: transactionForm.description,
        fromAccountNumber: transactionForm.fromAccountNumber,
        toAccountNumber: transactionForm.toAccountNumber,
        timestamp: new Date().toISOString()
      });
      // 刷新交易列表
      const response = await axios.get('http://localhost:8080/api/transactions', {
        params: {
          page: page - 1,
          size: rowsPerPage
        }
      });
      setTransactions(response.data);
      // 清空表单
      setTransactionForm({
    fromAccountNumber: '',
    toAccountNumber: '',
    type: 'TRANSFER',
    amount: '',
    description: ''
      });
    } catch (error) {
      console.error('创建交易失败:', error);
    }
  };

  // 处理分页变化
  const handlePageChange = (event, value) => {
    setPage(value);
  };

  return (
    <Container maxWidth="lg">
      {/* 交易创建表单 */}
      <Box component="form" onSubmit={handleSubmit} sx={{ mb: 4 }}>
        <Typography variant="h6" gutterBottom>
          创建交易
        </Typography>
        <Grid container spacing={3}>
          <Grid item xs={12} sm={6}>
            <Select
              fullWidth
              name="fromAccountNumber"
              value={transactionForm.fromAccountNumber}
              onChange={handleFormChange}
              required
            >
              <MenuItem value="">选择转出账户</MenuItem>
              {accounts.length > 0 ? (
                accounts.map(account => (
                  <MenuItem key={account.accountNumber} value={account.accountNumber}>
                    {account.accountNumber} - {account.accountHolder}
                  </MenuItem>
                ))
              ) : (
                <MenuItem disabled>
                  无可用账户
                </MenuItem>
              )}
            </Select>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Select
              fullWidth
              name="toAccountNumber"
              value={transactionForm.toAccountNumber}
              onChange={handleFormChange}
              required
            >
              <MenuItem value="">选择转入账户</MenuItem>
              {accounts.map(account => (
                <MenuItem key={account.accountNumber} value={account.accountNumber}>
                  {account.accountNumber} - {account.accountHolder}
                </MenuItem>
              ))}
            </Select>
          </Grid>
          <Grid item xs={12} sm={4}>
            <Select
              fullWidth
              name="type"
              value={transactionForm.type}
              onChange={handleFormChange}
              required
            >
              <MenuItem value="TRANSFER">转账</MenuItem>
              <MenuItem value="DEPOSIT">存款</MenuItem>
              <MenuItem value="WITHDRAWAL">取款</MenuItem>
            </Select>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              name="amount"
              label="金额"
              type="number"
              value={transactionForm.amount}
              onChange={handleFormChange}
              required
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              name="description"
              label="备注"
              value={transactionForm.description}
              onChange={handleFormChange}
            />
          </Grid>
          <Grid item xs={12}>
            <Button type="submit" variant="contained" color="primary">
              提交交易
            </Button>
          </Grid>
        </Grid>
      </Box>

      {/* 账户信息一览 */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h6" gutterBottom>
          账户信息
        </Typography>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>账户号</TableCell>
                <TableCell>账户持有人</TableCell>
                <TableCell>余额</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {accounts.map(account => (
                <TableRow key={account.id}>
                  <TableCell>{account.accountNumber}</TableCell>
                  <TableCell>{account.accountHolder}</TableCell>
                  <TableCell>¥{account.balance.toLocaleString()}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>

      {/* 交易信息一览 */}
      <Box>
        <Typography variant="h6" gutterBottom>
          交易记录
        </Typography>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
                <TableRow>
                  <TableCell>交易时间</TableCell>
                  <TableCell>类型</TableCell>
                  <TableCell>金额</TableCell>
                  <TableCell>备注</TableCell>
                  <TableCell>转出账户</TableCell>
                  <TableCell>转入账户</TableCell>
                </TableRow>
            </TableHead>
            <TableBody>
              {isLoading ? (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    加载中...
                  </TableCell>
                </TableRow>
              ) : error ? (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    加载交易数据失败
                  </TableCell>
                </TableRow>
              ) : transactions.length > 0 ? (
                transactions.map(transaction => (
                  <TableRow key={transaction.id}>
                    <TableCell>{transaction.date}</TableCell>
                    <TableCell>
                      {transaction.type === 'TRANSFER' && '转账'}
                      {transaction.type === 'DEPOSIT' && '存款'}
                      {transaction.type === 'WITHDRAWAL' && '取款'}
                    </TableCell>
                    <TableCell>¥{transaction.amount.toLocaleString()}</TableCell>
                    <TableCell>{transaction.description}</TableCell>
                    <TableCell>{transaction.fromAccountNumber	}</TableCell>
                    <TableCell>{transaction.toAccountNumber	}</TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    暂无交易记录
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
          <Pagination
            count={totalPages}
            page={page}
            onChange={handlePageChange}
            color="primary"
          />
        </Box>
      </Box>
    </Container>
  );
}

export default TransactionManagement;
