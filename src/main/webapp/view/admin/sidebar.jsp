<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
  <!-- Sidebar - Brand -->
  <a class="sidebar-brand d-flex align-items-center justify-content-center" href="dashboard.jsp">
    <div class="sidebar-brand-icon rotate-n-15">
      <i class="fas fa-tachometer-alt"></i>
    </div>
    <div class="sidebar-brand-text mx-3">Admin Panel</div>
  </a>
  <hr class="sidebar-divider my-0">
  <li class="nav-item ${pageContext.request.servletPath == '/dashboard.jsp' ? 'active' : ''}">
    <a class="nav-link" href="dashboard.jsp">
      <i class="fas fa-fw fa-tachometer-alt"></i>
      <span>Dashboard</span>
    </a>
  </li>
  <hr class="sidebar-divider">
  <li class="nav-item ${pageContext.request.servletPath == '/settings.jsp' ? 'active' : ''}">
    <a class="nav-link" href="settings.jsp">
      <i class="fas fa-fw fa-cog"></i>
      <span>Settings</span>
    </a>
  </li>
  <li class="nav-item ${pageContext.request.servletPath == '/user.jsp' ? 'active' : ''}">
    <a class="nav-link" href="user.jsp">
      <i class="fas fa-fw fa-user"></i>
      <span>User</span>
    </a>
  </li>
  <li class="nav-item ${pageContext.request.servletPath == '/payment.jsp' ? 'active' : ''}">
    <a class="nav-link" href="payment.jsp">
      <i class="fas fa-fw fa-credit-card"></i>
      <span>Payment</span>
    </a>
  </li>
  <li class="nav-item ${pageContext.request.servletPath == '/role.jsp' ? 'active' : ''}">
    <a class="nav-link" href="role.jsp">
      <i class="fas fa-fw fa-user-tag"></i>
      <span>Role</span>
    </a>
  </li>
  <li class="nav-item ${pageContext.request.servletPath == '/course.jsp' ? 'active' : ''}">
    <a class="nav-link" href="course.jsp">
      <i class="fas fa-fw fa-book"></i>
      <span>Course</span>
    </a>
  </li>
  <li class="nav-item ${pageContext.request.servletPath == '/slider.jsp' ? 'active' : ''}">
    <a class="nav-link" href="slider.jsp">
      <i class="fas fa-fw fa-images"></i>
      <span>Slider</span>
    </a>
  </li>
  <li class="nav-item ${pageContext.request.servletPath == '/exam.jsp' ? 'active' : ''}">
    <a class="nav-link" href="exam.jsp">
      <i class="fas fa-fw fa-clipboard-list"></i>
      <span>Exam</span>
    </a>
  </li>
  <li class="nav-item ${pageContext.request.servletPath == '/question.jsp' ? 'active' : ''}">
    <a class="nav-link" href="question.jsp">
      <i class="fas fa-fw fa-question-circle"></i>
      <span>Question</span>
    </a>
  </li>
  <li class="nav-item ${pageContext.request.servletPath == '/post.jsp' ? 'active' : ''}">
    <a class="nav-link" href="post.jsp">
      <i class="fas fa-fw fa-newspaper"></i>
      <span>Blog</span>
    </a>
  </li>
  <hr class="sidebar-divider d-none d-md-block">
  <div class="text-center d-none d-md-inline">
    <button class="rounded-circle border-0" id="sidebarToggle"></button>
  </div>
</ul>