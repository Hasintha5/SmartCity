<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Complaints - SmartCity360</title>
</head>
<body class="app line-c">
<c:set var="navActive" value="complaints"/>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<main class="app-main">

    <section class="glass flex flex-wrap items-center gap-x-4 gap-y-2 rounded-2xl px-5 py-3">
        <span class="badge-letter">C</span>
        <h1 class="text-xl font-semibold tracking-tight">Citizen complaints</h1>
        <span class="pill">Chain of Responsibility</span>
        <p class="min-w-[16rem] flex-1 text-sm text-slate-300">
            A complaint goes to the first officer. If it is too serious, it is passed up until someone can handle it.
        </p>
    </section>

    <div class="fill grid gap-4 lg:grid-cols-[24rem_minmax(0,1fr)]">

        <%-- LEFT: the form --%>
        <form method="post" action="${pageContext.request.contextPath}/complaints"
              class="glass scroll-y rounded-3xl p-5">

            <label for="citizen" class="field-label">Your name (optional)</label>
            <input id="citizen" name="citizen" type="text" maxlength="60" class="field"
                   value="<c:out value='${fCitizen}'/>">

            <label for="category" class="field-label mt-4">Category</label>
            <select id="category" name="category" class="field">
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat}" ${cat == fCategory ? 'selected' : ''}><c:out value="${cat}"/></option>
                </c:forEach>
            </select>

            <p class="field-label mt-4">How serious is it?</p>
            <div class="space-y-2">
                <c:forEach var="s" items="${severities}">
                    <label class="tile">
                        <input type="radio" name="severity" value="${s.code}" ${fSeverity == s.code ? 'checked' : ''}>
                        <div class="box"><p class="tile-title"><c:out value="${s.label}"/></p><p class="tile-note"><c:out value="${s.hint}"/></p></div>
                        <span class="mark" aria-hidden="true"></span>
                    </label>
                </c:forEach>
            </div>

            <label for="description" class="field-label mt-4">What is the problem?</label>
            <textarea id="description" name="description" rows="3" maxlength="500" required
                      class="field resize-none"><c:out value="${fDescription}"/></textarea>

            <p class="field-label mt-4">Who is on duty today?</p>
            <div class="grid grid-cols-3 gap-2">
                <label class="tile">
                    <input type="checkbox" name="staff" value="on" ${staffOn ? 'checked' : ''}>
                    <div class="box text-center" style="padding: .6rem .3rem"><p class="tile-title">Local staff</p></div>
                    <span class="sr-only">on duty</span>
                </label>
                <label class="tile">
                    <input type="checkbox" name="supervisor" value="on" ${supervisorOn ? 'checked' : ''}>
                    <div class="box text-center" style="padding: .6rem .3rem"><p class="tile-title">Supervisor</p></div>
                    <span class="sr-only">on duty</span>
                </label>
                <label class="tile">
                    <input type="checkbox" name="manager" value="on" ${managerOn ? 'checked' : ''}>
                    <div class="box text-center" style="padding: .6rem .3rem"><p class="tile-title">City manager</p></div>
                    <span class="sr-only">on duty</span>
                </label>
            </div>
            <p class="mt-2 text-xs text-slate-400">Switch someone off to see the complaint skip them.</p>

            <div class="mt-5 flex flex-wrap gap-3">
                <button type="submit" name="action" value="submit" class="btn">Send complaint</button>
                <button type="submit" name="action" value="clear" formnovalidate class="btn-ghost">Clear all</button>
            </div>
        </form>

        <%-- RIGHT: chain, path of the last complaint, history --%>
        <div class="grid min-h-0 gap-4 lg:overflow-y-auto xl:grid-cols-2 xl:grid-rows-[minmax(0,1fr)]">

            <div class="flex min-h-0 flex-col gap-4 lg:overflow-y-auto">

                <section class="glass shrink-0 rounded-3xl p-5">
                    <h2 class="panel-title">Escalation chain right now</h2>
                    <div class="mt-3 flex flex-wrap items-center gap-2">
                        <c:choose>
                            <c:when test="${empty chainRoles}"><span class="status bad">Nobody is on duty</span></c:when>
                            <c:otherwise>
                                <c:forEach var="role" items="${chainRoles}" varStatus="st">
                                    <span class="pill"><c:out value="${role}"/></span>
                                    <c:if test="${not st.last}"><span class="text-xs text-slate-400" aria-hidden="true">then</span></c:if>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <c:if test="${not empty message}">
                        <p class="mt-3 text-sm font-semibold" role="status"><c:out value="${message}"/></p>
                    </c:if>
                </section>

                <c:choose>
                    <c:when test="${not empty latest}">
                        <section class="glass shrink-0 rounded-3xl p-5">
                            <div class="flex flex-wrap items-center justify-between gap-2">
                                <h2 class="panel-title">Complaint #<c:out value="${latest.id}"/> path</h2>
                                <span class="status ${latest.resolved ? 'ok' : 'bad'}">
                                    <c:out value="${latest.status}"/><c:if test="${latest.resolved}"> by <c:out value="${latest.resolvedBy}"/></c:if>
                                </span>
                            </div>
                            <p class="mt-2 text-sm text-slate-300"><c:out value="${latest.description}"/></p>

                            <ol class="mt-4 space-y-2">
                                <c:forEach var="step" items="${latest.steps}" varStatus="st">
                                    <li class="row flex gap-3 p-3">
                                        <span class="badge-letter !h-7 !w-7 !rounded-full !text-xs"><c:out value="${st.count}"/></span>
                                        <div class="min-w-0">
                                            <p class="text-sm font-semibold">
                                                <c:out value="${step.handler}"/>
                                                <span class="status ml-1 ${step.action == 'Resolved' ? 'ok' : (step.action == 'Escalated' ? 'warn' : 'bad')}"><c:out value="${step.action}"/></span>
                                            </p>
                                            <p class="text-xs text-slate-300"><c:out value="${step.note}"/></p>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ol>
                        </section>
                    </c:when>
                    <c:otherwise>
                        <section class="glass shrink-0 rounded-3xl p-5">
                            <h2 class="panel-title">Complaint path</h2>
                            <p class="mt-2 text-sm text-slate-400">Send a complaint to see who looked at it, step by step.</p>
                        </section>
                    </c:otherwise>
                </c:choose>
            </div>

            <section class="glass flex min-h-[12rem] flex-col rounded-3xl p-5">
                <h2 class="panel-title">All complaints</h2>
                <c:choose>
                    <c:when test="${empty complaints}">
                        <p class="mt-2 text-sm text-slate-400">No complaints yet.</p>
                    </c:when>
                    <c:otherwise>
                        <ul class="scroll-y mt-3 min-h-0 flex-1 space-y-2">
                            <c:forEach var="h" items="${complaints}">
                                <li class="row flex flex-wrap items-center justify-between gap-2 px-4 py-2.5">
                                    <div class="min-w-0">
                                        <p class="text-sm font-semibold">#<c:out value="${h.id}"/> <c:out value="${h.category}"/></p>
                                        <p class="text-xs text-slate-300"><c:out value="${h.citizen}"/> &middot; <c:out value="${h.severity.label}"/> severity</p>
                                    </div>
                                    <span class="status ${h.resolved ? 'ok' : 'bad'}">
                                        <c:out value="${h.status}"/><c:if test="${h.resolved}"> by <c:out value="${h.resolvedBy}"/></c:if>
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>
    </div>
</main>
</body>
</html>
