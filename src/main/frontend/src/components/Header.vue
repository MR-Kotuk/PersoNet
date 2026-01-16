<template>
  <div class="header">

    <div class="logo">
      <a href="/">
        <img src="../assets/logo.png" alt="home">
      </a>
    </div>

    <div class="menu">
      <a href="/">Home</a>
      <a href="/contacts">Contacts</a>
      <a href="/about">About</a>
      <a href="/account">Account</a>
      <a href="/settings">Settings</a>
    </div>
  </div>
</template>

<style scoped>
.header {
  position: sticky;
  top: 0;

  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;

  padding: 5px 13px;
  background: black;

  border-bottom: 1px #282828 solid;
}

.logo {
  justify-self: start;
}

.logo img {
  width: 48px;
}

.menu {
  justify-self: center;
  display: flex;
  gap: 30px;
}

.menu a {
  position: relative;
  font-weight: bold;
  color: white;
  text-decoration: none;
  padding-bottom: 3px;
  transition: color 0.3s;
}

.menu a::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 0;
  height: 2px;
  background-color: #0a62b4;
  transition: width 0.3s ease;
}

.menu a:hover::after {
  width: 100%;
}

.menu a:hover {
  color: #208af3;
}
</style>