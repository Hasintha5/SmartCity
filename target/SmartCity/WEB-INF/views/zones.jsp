<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Smart zones - SmartCity360</title>
</head>
<body class="app line-b">
<c:set var="navActive" value="zones"/>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="app-main">

    <section class="glass flex flex-wrap items-center gap-x-4 gap-y-2 rounded-2xl px-5 py-3">
        <span class="badge-letter">B</span>
        <h1 class="text-xl font-semibold tracking-tight">Smart zones</h1>
        <span class="pill">Composite</span>
        <p class="min-w-[16rem] flex-1 text-sm text-slate-300">
            City, districts, streets and buildings form one tree. A command sent to any level reaches every building below it.
        </p>
    </section>

    <div class="fill grid gap-4 lg:grid-cols-[20rem_minmax(0,1fr)]">

        <%-- LEFT: totals, last message, reset --%>
        <aside class="glass scroll-y flex flex-col gap-4 rounded-3xl p-5">
            <div class="grid grid-cols-2 gap-3 lg:grid-cols-1">
                <div class="stat">
                    <p class="stat-label">City usage</p>
                    <p class="stat-value"><c:out value="${totalUsage}"/> <span class="text-sm font-normal text-slate-300">kWh</span></p>
                </div>
                <div class="stat">
                    <p class="stat-label">Buildings with power</p>
                    <p class="stat-value"><c:out value="${buildingsOn}"/> <span class="text-sm font-normal text-slate-300">of <c:out value="${buildings}"/></span></p>
                </div>
            </div>

            <div>
                <p class="panel-title">Last command</p>
                <c:choose>
                    <c:when test="${not empty message}">
                        <p class="mt-2 rounded-xl border-l-4 bg-white/5 p-3 text-sm font-semibold" style="border-left-color: var(--c)" role="status"><c:out value="${message}"/></p>
                    </c:when>
                    <c:otherwise>
                        <p class="mt-2 text-sm text-slate-400">Nothing sent yet. Use the buttons on any row.</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/zones" class="mt-auto">
                <button type="submit" name="action" value="reset" class="btn-ghost w-full">Reset city</button>
            </form>
        </aside>

        <%-- RIGHT: the tree --%>
        <section class="glass scroll-y rounded-3xl p-4 md:p-5">
            <ul class="space-y-2">
                <c:forEach var="r" items="${rows}">
                    <li class="row flex flex-wrap items-center justify-between gap-x-4 gap-y-2 px-4 py-2.5 ${r.powerOn ? '' : 'opacity-60'}"
                        style="margin-left: ${r.depth * 1.5}rem">

                        <div class="min-w-0">
                            <div class="flex flex-wrap items-center gap-2">
                                <span class="pill"><c:out value="${r.level}"/></span>
                                <span class="font-semibold"><c:out value="${r.name}"/></span>
                            </div>
                            <p class="mt-1 text-xs text-slate-300">
                                <c:out value="${r.usageText}"/> kWh
                                <c:if test="${not r.leaf}"> &middot; <c:out value="${r.buildings}"/> buildings</c:if>
                                &middot;
                                <span class="${r.powerOn ? 'text-emerald-300' : 'text-rose-300'}">${r.powerOn ? 'Power on' : 'Power off'}</span>
                            </p>
                        </div>

                        <form method="post" action="${pageContext.request.contextPath}/zones" class="flex flex-wrap gap-1.5">
                            <input type="hidden" name="target" value="${fn:escapeXml(r.name)}">
                            <button type="submit" name="action" value="report" class="btn-mini">Report usage</button>
                            <button type="submit" name="action" value="shutdown" class="btn-mini text-rose-300">Shut down</button>
                            <button type="submit" name="action" value="restore" class="btn-mini text-emerald-300">Restore</button>
                        </form>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </div>
</main>
</body>
</html>
