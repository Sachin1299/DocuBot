import React from 'react';
import { Navbar, Container } from 'react-bootstrap';
import Logout from './Logout';
import ThemeToggle from './ThemeToggle';

export default function Header() {
  return (
    <Navbar className='header' variant="dark" expand="lg" sticky="top">
      <Container>
        <Navbar.Brand className='header' href="/home">🧠 DocuBot</Navbar.Brand>
        <ThemeToggle/>
        <Logout />
      </Container>
    </Navbar>
  );
}

