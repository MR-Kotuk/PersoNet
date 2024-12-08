import { Routes, Route } from "react-router-dom";
import { HomePage } from "./pages/home/homePage";
import "./App.css";
import { Layout } from "./layout/layout";
import { AboutPage } from "./pages/about/aboutPage";
import { PersonPage } from "./pages/person/personPage";
import { RegisterPage } from "./pages/authorization/register/register";
import { AuthPage } from "./pages/authorization/authentification/auth";

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route path="home" element={<HomePage />} />
          <Route path="about" element={<AboutPage />} />
          <Route path="person" element={<PersonPage />} />
        </Route>

        <Route path="/register" element={<RegisterPage />} />
        <Route path="/auth" element={<AuthPage />} />
      </Routes>
    </div>
  );
}

export default App;
