
document.addEventListener('DOMContentLoaded', function () {
    const productTable = document.getElementById('productTable');
    const cartTable = document.getElementById('cartTable');
    const checkoutBtn = document.getElementById('checkoutBtn');
    const totalAmountElement = document.getElementById('totalAmount');
    const productSearch = document.getElementById('productSearch');
    const searchBtn = document.getElementById('searchBtn');
    const loggedInCustomerName = document.getElementById('loggedInCustomerName');
    const loggedInCustomerEmail = document.getElementById('loggedInCustomerEmail');
    const customerIdInput = document.getElementById('customerId');
    const logoutBtn = document.getElementById('logoutBtn');

    let products = [];
    let currentCustomer = null;
    let cart = [];
    let filteredProducts = [];

    const API_BASE_URL = 'http://localhost:8082/api/v1';

    async function init() {
        try {
            await verifyAuthentication();
            await fetchCurrentCustomer();
            await fetchProducts();
            filteredProducts = [...products];
            renderProducts();
            renderCart();
        } catch (error) {
            console.error('Initialization error:', error);
            redirectToLogin();
        }
    }

    async function verifyAuthentication() {
        const token = localStorage.getItem('authToken');
        if (!token) {
            redirectToLogin();
            return false;
        }

        try {
            const decodedToken = parseJwt(token);
            if (!decodedToken) {
                redirectToLogin();
                return false;
            }

            // Check token expiration if exp claim exists
            if (decodedToken.exp) {
                const now = Math.floor(Date.now() / 1000);
                if (now > decodedToken.exp) {
                    redirectToLogin();
                    return false;
                }
            }

            // Set basic user info from token
            loggedInCustomerName.textContent = decodedToken.name || decodedToken.sub || 'Customer';
            loggedInCustomerEmail.textContent = decodedToken.email || decodedToken.sub;

            return true;
        } catch (error) {
            console.error('Token verification error:', error);
            redirectToLogin();
            return false;
        }
    }

    function parseJwt(token) {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(c =>
                '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
            ).join(''));
            return JSON.parse(jsonPayload);
        } catch (e) {
            console.error('Error parsing JWT:', e);
            return null;
        }
    }

    async function fetchCurrentCustomer() {
        const token = localStorage.getItem('authToken');
        if (!token) {
            redirectToLogin();
            return;
        }

        try {
            const decodedToken = parseJwt(token);
            if (!decodedToken) {
                throw new Error('Invalid token');
            }

            // Create basic customer info from token
            currentCustomer = {
                id: decodedToken.sub || 'unknown',
                name: decodedToken.name || decodedToken.email?.split('@')[0] || 'Customer',
                email: decodedToken.email || decodedToken.sub || 'unknown@example.com'
            };

            // Update UI
            loggedInCustomerName.textContent = currentCustomer.name;
            loggedInCustomerEmail.textContent = currentCustomer.email;
            customerIdInput.value = currentCustomer.id;

            // Try to fetch additional customer data if email exists
            if (decodedToken.email) {
                try {
                    const response = await fetch(`${API_BASE_URL}/customers/by-email?email=${encodeURIComponent(decodedToken.email)}`, {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    });

                    if (response.ok) {
                        const customerData = await response.json();
                        // Merge with existing customer info
                        currentCustomer = { ...currentCustomer, ...customerData };
                        loggedInCustomerName.textContent = currentCustomer.name;
                        customerIdInput.value = currentCustomer.id;
                    }
                } catch (error) {
                    console.log('Could not fetch additional customer data, using token info:', error);
                }
            }
        } catch (error) {
            console.error('Error processing customer details:', error);
            // Ensure we have minimal customer data even if everything fails
            currentCustomer = {
                id: 'unknown',
                name: 'Customer',
                email: 'unknown@example.com'
            };
            customerIdInput.value = currentCustomer.id;
        }
    }

    function redirectToLogin() {
        localStorage.removeItem('authToken');
        window.location.href = '../login.html';
    }

    async function fetchProducts() {
        try {
            const response = await fetch('http://localhost:8082/api/v1/products/getAll');
            products = await response.json();
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    }

    function filterProducts(searchTerm) {
        if (!searchTerm) {
            filteredProducts = [...products];
            return;
        }

        const term = searchTerm.toLowerCase();
        filteredProducts = products.filter(product =>
            product.name.toLowerCase().includes(term) ||
            product.id.toString().includes(term)
        );
    }

    function renderProducts() {
        productTable.innerHTML = '';

        if (filteredProducts.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="5" class="text-center">No products found</td>`;
            productTable.appendChild(row);
            return;
        }

        filteredProducts.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>$${product.price.toFixed(2)}</td>
                <td><input type="number" class="form-control quantity-input" value="1" min="1" data-product-id="${product.id}"></td>
                <td><button class="btn btn-sm btn-primary add-to-cart-btn" data-product-id="${product.id}">
                    <i class="bi bi-cart-plus"></i> Add
                </button></td>
            `;
            productTable.appendChild(row);
        });
    }

    function renderCart() {
        cartTable.innerHTML = '';
        let totalAmount = 0;

        if (cart.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="5" class="text-center">Your cart is empty</td>`;
            cartTable.appendChild(row);
            totalAmountElement.textContent = `$0.00`;
            return;
        }

        cart.forEach((item, index) => {
            const row = document.createElement('tr');
            const itemTotal = item.price * item.quantity;
            totalAmount += itemTotal;

            row.innerHTML = `
                <td>${item.productName} (ID: ${item.productId})</td>
                <td>
                    <div class="d-flex align-items-center">
                        <button class="btn btn-sm btn-outline-secondary decrement" data-index="${index}">-</button>
                        <span class="mx-2">${item.quantity}</span>
                        <button class="btn btn-sm btn-outline-secondary increment" data-index="${index}">+</button>
                    </div>
                </td>
                <td>$${item.price.toFixed(2)}</td>
                <td>$${itemTotal.toFixed(2)}</td>
                <td><button class="btn btn-sm btn-danger remove-item" data-index="${index}">
                    <i class="bi bi-trash"></i>
                </button></td>
            `;
            cartTable.appendChild(row);
        });

        totalAmountElement.textContent = `$${totalAmount.toFixed(2)}`;
    }

    function addToCart(productId, quantity) {
        const product = products.find(p => p.id === productId);

        if (product && quantity > 0) {
            const existingItemIndex = cart.findIndex(item => item.productId === productId);

            if (existingItemIndex >= 0) {
                cart[existingItemIndex].quantity += quantity;
            } else {
                cart.push({
                    productId: product.id,
                    productName: product.name,
                    price: product.price,
                    quantity: quantity
                });
            }

            const button = document.querySelector(`.add-to-cart-btn[data-product-id="${productId}"]`);
            if (button) {
                button.classList.add('added-to-cart');
                setTimeout(() => button.classList.remove('added-to-cart'), 500);
            }

            renderCart();
        }
    }

    productTable.addEventListener('click', function (e) {
        if (e.target.classList.contains('add-to-cart-btn') ||
            e.target.closest('.add-to-cart-btn')) {
            const button = e.target.classList.contains('add-to-cart-btn') ?
                e.target : e.target.closest('.add-to-cart-btn');
            const productId = parseInt(button.dataset.productId);
            const quantityInput = document.querySelector(`.quantity-input[data-product-id="${productId}"]`);
            const quantity = parseInt(quantityInput.value);

            addToCart(productId, quantity);
        }
    });

    searchBtn.addEventListener('click', function () {
        filterProducts(productSearch.value);
        renderProducts();
    });

    productSearch.addEventListener('keyup', function (e) {
        if (e.key === 'Enter') {
            filterProducts(productSearch.value);
            renderProducts();
        }
    });

    cartTable.addEventListener('click', function (e) {
        if (e.target.classList.contains('remove-item') ||
            e.target.closest('.remove-item')) {
            const button = e.target.classList.contains('remove-item') ?
                e.target : e.target.closest('.remove-item');
            const index = parseInt(button.dataset.index);
            cart.splice(index, 1);
            renderCart();
        }

        if (e.target.classList.contains('increment') ||
            e.target.closest('.increment')) {
            const button = e.target.classList.contains('increment') ?
                e.target : e.target.closest('.increment');
            const index = parseInt(button.dataset.index);
            cart[index].quantity += 1;
            renderCart();
        }

        if (e.target.classList.contains('decrement') ||
            e.target.closest('.decrement')) {
            const button = e.target.classList.contains('decrement') ?
                e.target : e.target.closest('.decrement');
            const index = parseInt(button.dataset.index);
            if (cart[index].quantity > 1) {
                cart[index].quantity -= 1;
                renderCart();
            }
        }
    });

    checkoutBtn.addEventListener('click', async function () {
        if (cart.length === 0) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Your cart is empty!",
            });
            return;
        }

        const token = localStorage.getItem('authToken');
        if (!token) {
            redirectToLogin();
            return;
        }

        try {
            const decodedToken = parseJwt(token);
            if (decodedToken.exp && (Date.now() >= decodedToken.exp * 1000)) {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Session expired. Please login again.",
                });
                redirectToLogin();
                return;
            }

            if (!currentCustomer || !currentCustomer.id) {
                await fetchCurrentCustomer();
                if (!currentCustomer || !currentCustomer.id) {
                    throw new Error('Could not load customer data');
                }
            }

            const totalAmount = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);

            const orderDTO = {
                customerId: currentCustomer.id,
                totalAmount: totalAmount,
                orderItems: cart.map(item => ({
                    productId: item.productId,
                    quantity: item.quantity,
                    price: item.price
                }))
            };

            console.log('Submitting order:', orderDTO);

            const response = await fetch(`${API_BASE_URL}/orders`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(orderDTO)
            });

            if (!response.ok) {
                if (response.status === 403) {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Session expired or insufficient permissions. Please login again.",
                    });
                    redirectToLogin();
                    return;
                }
                const errorText = await response.text();
                throw new Error(errorText || 'Failed to place order');
            }

            const order = await response.json();

            const itemsDescription = cart.map(item => `${item.productName} (x${item.quantity})`).join(', ');

            const hashResponse = await fetch(`${API_BASE_URL}/payment/hash`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    order_id: order.id.toString(),
                    amount: totalAmount.toFixed(2),
                    currency: "LKR"
                })
            });

            if (!hashResponse.ok) {
                throw new Error('Failed to generate payment hash');
            }

            const paymentData = await hashResponse.json();

            // Populate PayHere form
            const form = document.getElementById('payhere-form');
            form.elements['merchant_id'].value = paymentData.merchant_id;
            form.elements['order_id'].value = order.id;
            form.elements['items'].value = itemsDescription;
            form.elements['amount'].value = totalAmount.toFixed(2);
            form.elements['first_name'].value = currentCustomer.name.split(' ')[0] || 'Customer';
            form.elements['last_name'].value = currentCustomer.name.split(' ')[1] || '';
            form.elements['email'].value = currentCustomer.email;
            form.elements['phone'].value = currentCustomer.phone || '0771234567';
            form.elements['address'].value = currentCustomer.address || 'No Address Provided';
            form.elements['city'].value = currentCustomer.city || 'Colombo';
            form.elements['hash'].value = paymentData.hash;

            form.submit();

        } catch (error) {
            console.error('Checkout error:', error);
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Checkout failed:" + error.message,
            });
        }
    });

    logoutBtn.addEventListener('click', function() {
        localStorage.removeItem('authToken');
        window.location.href = '../login.html';
    });

    init();
});
