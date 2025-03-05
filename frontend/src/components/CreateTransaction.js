import React, { useState } from 'react';
import { TextField, Button, Grid, Typography } from '@mui/material';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function CreateTransaction({ fetchTransactions }) {
  const [formData, setFormData] = useState({
    type: 'INCOME',
    amount: '',
    description: ''
  });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/transactions', formData);
      fetchTransactions();
      navigate('/');
    } catch (error) {
      console.error('Error creating transaction:', error);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <form onSubmit={handleSubmit}>
      <Typography variant="h5" gutterBottom>
        创建新交易
      </Typography>
      <Grid container spacing={3}>
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
            label="描述"
            name="description"
            value={formData.description}
            onChange={handleChange}
            required
          />
        </Grid>
        <Grid item xs={12}>
          <Button type="submit" variant="contained" color="primary">
            提交
          </Button>
        </Grid>
      </Grid>
    </form>
  );
}

export default CreateTransaction;
