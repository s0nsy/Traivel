import { createBrowserRouter } from "react-router-dom";
import Header from "../Pages/header";
import Main from "../Pages/Main";
import Calendar from "../Pages/Calendar";
import List from "../Pages/List/List";

const router = createBrowserRouter([
    {
        path: "/",
        element: (
            <div>
                <Header />
                <Main />
            </div>
        ),
    },
    {
        path: "/temp",
        element: (
            <div>
                <Header />
                <Calendar />
            </div>
        ),
    },
    {
        path: "/lists",
        element: (
            <div>
                <Header />
                <List />
            </div>
        ),
    },
]);

export default router;
